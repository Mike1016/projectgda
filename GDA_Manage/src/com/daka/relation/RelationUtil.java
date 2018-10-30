package com.daka.relation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RelationUtil
{

	private static int QUEUE_COUNT = 100;

	// 树ID
	private static AtomicInteger incrementTreeId = new AtomicInteger(0);

	// 每个树ID对应树中的所有userIds
	private static Map<Integer, List<String>> treeKey2UserId = new ConcurrentHashMap<>();

	// 树ID对应的树
	private static Map<Integer, RelationTree> relationTree = new ConcurrentHashMap<>();

	// 队列
	@SuppressWarnings("unused")
	private static final LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(QUEUE_COUNT);

	// 线程池
	@SuppressWarnings("unused")
	private static final ExecutorService executor = new ThreadPoolExecutor(20, 100, 10L, TimeUnit.MINUTES,
			new ArrayBlockingQueue<Runnable>(200));

	private static Lock lock = new ReentrantLock();

	/**
	 * 通过ID获取所有的团队
	 * 
	 * @param id
	 *            -- 自己的ID
	 * @return 团队ID#level 深度优先
	 */
	public static List<String> findDownTeam(String id)
	{
		List<String> result = new ArrayList<>();
		int treeId = -1;
		for (Entry<Integer, List<String>> entry : treeKey2UserId.entrySet())
		{
			if (entry.getValue().contains(id))
			{
				treeId = entry.getKey();
				break;
			}
		}

		// 2. 否则，更新树结构
		RelationTree currentTree = relationTree.get(treeId);
		if (null == currentTree)
		{
			return result;
		}

		findChildrenById(currentTree, id, result, false);
		result.remove(id);
		return result;
	}

	// public static void main(String[] args)
	// {
	// RelationTree root = new RelationTree();
	// root.setId("1");
	// root.setLevel(0);
	// root.setChildren(new ArrayList<RelationTree>());
	// RelationTree relationTree2 = new RelationTree("2");
	// relationTree2.setLevel(root.getLevel() + 1);
	// root.getChildren().add(relationTree2);
	// RelationTree relationTree3 = new RelationTree("3");
	// root.getChildren().add(relationTree3);
	// relationTree3.setLevel(relationTree3.getLevel() + 1);
	// RelationTree relationTree4 = new RelationTree("4");
	// relationTree4.setLevel(relationTree2.getLevel() + 1);
	// relationTree2.getChildren().add(relationTree4);
	// RelationTree relationTree5 = new RelationTree("5");
	// relationTree5.setLevel(relationTree2.getLevel() + 1);
	// relationTree2.getChildren().add(relationTree5);
	//
	// RelationTree relationTree6 = new RelationTree("6");
	// relationTree6.setLevel(relationTree4.getLevel() + 1);
	// relationTree4.getChildren().add(relationTree6);
	//
	// updateTreeChildren(root, "7", "4", false);
	// updateTreeChildren(root, "8", "7", false);
	// updateTreeChildren(root, "9", "3", false);
	// updateTreeChildren(root, "10", "1", false);
	//
	// List<String> result = new ArrayList<>();
	// findChildrenById(root, "2", result, false);
	// System.out.println("");
	// }

	private static void findChildrenById(RelationTree currentTree, String id, List<String> result, boolean isStop)
	{
		if (currentTree.getId().equals(id))
		{
			findChildrenById(currentTree, result);
			return;
		}
		List<RelationTree> children = currentTree.getChildren();
		if (null == children || children.isEmpty() || isStop)
		{
			return;
		}
		for (RelationTree child : children)
		{
			if (!child.getId().equals(id))
			{
				findChildrenById(child, id, result, isStop);
				continue;
			}
			findChildrenById(child, result);
		}

	}

	private static void findChildrenById(RelationTree currentTree, List<String> result)
	{
		List<RelationTree> children = currentTree.getChildren();
		result.add(currentTree.getId() + "#" + currentTree.getLevel());
		if (children == null || children.isEmpty())
		{
			return;
		}
		for (RelationTree child : children)
		{
			findChildrenById(child, result);
		}
	}

	public static void initTree(List<Map<String, String>> param)
	{

		for (Map<String, String> map : param)
		{
			updateTreeRelation(String.valueOf(map.get("id")), String.valueOf(map.get("pId")));
		}
	}

	/**
	 * 更新树结构
	 * 
	 * @param id
	 *            --本身的ID
	 * @param pId
	 *            --父ID
	 */
	public static void updateTreeRelation(String id, String pId)
	{
		if(null == pId || pId.isEmpty() || null == id || id.isEmpty()){
			return;
		}
		lock.lock();
		try
		{
			// 1. 获取父级ID是属于那棵树
			int treeId = -1;
			for (Entry<Integer, List<String>> entry : treeKey2UserId.entrySet())
			{
				if (entry.getValue().contains(pId))
				{
					treeId = entry.getKey();
					break;
				}
			}

			// 2. 如果存在的树中没有，则自己创建一棵树
			if (treeId == -1)
			{
				RelationTree temp = new RelationTree();
				temp.setId(id);
				temp.setLevel(0);
				temp.setChildren(new ArrayList<RelationTree>());
				// temp.getChildren().add(new RelationTree(id, temp.getLevel() +
				// 1));

				relationTree.put(incrementTreeId.get(), temp);

				List<String> userIds = new ArrayList<>();
				userIds.add(id);
				treeKey2UserId.put(incrementTreeId.get(), userIds);
				incrementTreeId.incrementAndGet();
				return;
			}

			// 2. 否则，更新树结构
			RelationTree currentTree = relationTree.get(treeId);
			treeKey2UserId.get(treeId).add(id);
			updateTreeChildren(currentTree, id, pId, false);
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			lock.unlock();
		}

	}

	private static void updateTreeChildren(RelationTree currentTree, String childrenId, String pId, boolean isStop)
	{
		if (pId.equals(currentTree.getId()))
		{
			RelationTree temp = new RelationTree();
			temp.setId(childrenId);
			temp.setLevel(currentTree.getLevel() + 1);
			temp.setChildren(new ArrayList<RelationTree>());
			currentTree.getChildren().add(temp);
			return;
		}
		List<RelationTree> children = currentTree.getChildren();
		if (null == children || children.isEmpty() || isStop)
		{
			return;
		}
		for (RelationTree child : children)
		{
			if (child.getId().equals(pId))
			{
				RelationTree temp = new RelationTree();
				temp.setId(childrenId);
				temp.setLevel(child.getLevel() + 1);
				temp.setChildren(new ArrayList<RelationTree>());
				child.getChildren().add(temp);
				isStop = true;
				updateTreeChildren(temp, childrenId, pId, isStop);
			} else
			{
				updateTreeChildren(child, childrenId, pId, isStop);
			}
		}
	}
}
