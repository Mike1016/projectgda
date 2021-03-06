	var lottery = {
		index: -1, //当前转动到哪个位置，起点位置
		count: 0, //总共有多少个位置
		timer: 0, //setTimeout的ID，用clearTimeout清除
		speed: 20, //初始转动速度
		times: 0, //转动次数
		cycle: 50, //转动基本次数：即至少需要转动多少次再进入抽奖环节
		prize: 0, //中奖位置
		init: function(id) {
			if($('#' + id).find('.lottery-unit').length > 0) {
				$lottery = $('#' + id);
				$units = $lottery.find('.lottery-unit');
				this.obj = $lottery;
				this.count = $units.length;
				$lottery.find('.lottery-unit.lottery-unit-' + this.index).addClass('active');
			};
		},
		roll: function() {
			var index = this.index;
			var count = this.count;
			var lottery = this.obj;
			$(lottery).find('.lottery-unit.lottery-unit-' + index).removeClass('active');
			index += 1;
			if(index > count - 1) {
				index = 0;
			};
			$(lottery).find('.lottery-unit.lottery-unit-' + index).addClass('active');
			this.index = index;
			return false;
		},
		stop: function(index) {
			this.prize = index;
			return false;
		}
	};
	var bh = '';
	function roll() {
		lottery.times += 1;
		lottery.roll(); //转动过程调用的是lottery的roll方法，这里是第一次调用初始化

		if(lottery.times > lottery.cycle + 10 && lottery.prize == lottery.index) {
			clearTimeout(lottery.timer);
		
			layer.open({
			   type: 1,			           
			   shadeClose: true,
			   shade: false,
			   maxmin: true, //开启最大化最小化按钮
			   area: ['893px', '600px'],
			   content: $("#info").html()
			});
			
			lottery.prize = -1;
			lottery.times = 0;
			click = false;
		} else {
			if(lottery.times < lottery.cycle) {
				lottery.speed -= 10;
			} else if(lottery.times == lottery.cycle) {
				
				//var index = Math.random() * (lottery.count) | 0; //静态演示，随机产生一个奖品序号，实际需请求接口产生
				console.log(bh);
				lottery.prize = bh;
			} else {
				if(lottery.times > lottery.cycle + 10 && ((lottery.prize == 0 && lottery.index == 7) || lottery.prize == lottery.index + 1)) {
					lottery.speed += 110;
				} else {
					lottery.speed += 20;
				}
			}
			if(lottery.speed < 40) {
				lottery.speed = 40;
			};
			lottery.timer = setTimeout(roll, lottery.speed); //循环调用
		}
		return false;
	}

	var click = false;

	window.onload = function() {
		lottery.init('lottery');

		$('.draw-btn').click(function() {
			if(click) { //click控制一次抽奖过程中不能重复点击抽奖按钮，后面的点击不响应
				return false;
			} else {
				ajax({
					type: 'get',
					url: '/app/tray/luckDraw.do',
					data: {
						sessionId: sid,
						trayGrade: 1
					},
					success: function(res) {
						console.log(res);
						if(res.status == 1) {
							lottery.speed = 100;
							console.log(res.data.trayNo)
							bh = res.data.trayNo;
							roll(); //转圈过程不响应click事件，会将click置为false
							$('.jlzs').text(res.data.prizes);
							click = true; //一次抽奖完成后，设置click为true，可继续抽奖		
							return false;
						} else {
							mui.toast(res.msg);
						}
					}

				});
			}

		});
	};