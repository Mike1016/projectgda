package com.daka.relation;

import java.util.ArrayList;
import java.util.List;

public class RelationTree
{

	private String id;

	private int level;

	private List<RelationTree> children;

	public RelationTree()
	{

	}

	public RelationTree(String id)
	{
		this.id = id;
		this.children = new ArrayList<>();
	}

	public RelationTree(String id, int level)
	{
		this.id = id;
		this.level = level;
		this.children = new ArrayList<>();
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public List<RelationTree> getChildren()
	{
		return children;
	}

	public void setChildren(List<RelationTree> children)
	{
		this.children = children;
	}

}
