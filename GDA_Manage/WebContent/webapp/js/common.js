var sid = $.trim(localStorage['sessionId']);

var height = $(window).height();
var width = $(window).width();
$(".content,.mask,.showMask").css("height",height);
$(".content,.mask,.showMask").css("width",width);

$('.get_code').on('tap',function(){
	var phone = $(".phone").val();
	if(phone!= ''){
		if(phone.length == 11){	
			$('.get_code').attr("disabled",true);		
			ajax({
				url:host+"/customer/authcode.do",
				type:'get',
				data:{
					phone:phone
				},
				success:function(res){
					if(res.status == 1){						
						var s = 120;
						$('.get_code').attr("disabled",true);
						var time = setInterval(function(){
							if(s>0){
								$(".get_code").text(s+'秒')
								s--;
							}else{
								clearInterval(time);
								timer = null;
								$('.get_code').attr("disabled",false);
								$('.get_code').text("重新获取");
							}
						},1000)						
						mui.toast(res.msg)
					}else{
						mui.toast(res.msg)
					}
					
				},
				error:function(){
					$('.get_code').attr("disabled",false);
				}
				
			})
			
		}else{
			mui.toast("请输入正确的手机号码");
		}
	}else{
		mui.toast('请输入手机号')
	}
});


$('.back').on('tap',function(){
	history.back(-1)
});
$('.back1').on('tap',function(){
	location.href = 'index.html'
});



function contdown_sub(time,actime,k){
	nowTime = parseInt(time) + 1000;
	var now = nowTime; 
	var end1=parseInt(actime);//活动开始时间
	var end = end1+24*60*60*1000; //6048000000 活动持续时间
	var s=parseInt((end-now)/1000);//总秒数
	var d=parseInt(s/3600/24);
	var h=parseInt(s%(3600*24)/3600);
	if(h<10){
		h="0"+h;
	}
	var m=parseInt(s%3600/60);
	if(m<10){
		m="0"+m;
	}
	var s=s%60;
	if(s<10){
		s="0"+s;
	}
	console.log($(".countdown"+k));
	$(".countdown"+k).html(h+":"+m+":"+s);
	//var timer1 = setTimeout('contdown_sub()',1000);
//	if(end-now<=500){
//		clearTimeout(timer1);
//		mui.toast("若界面未刷新，请尝试手动刷新");
//		location.reload();
//		return;
//	}
}
function contdown(g,n,x){
	var nowTime;
	var activityTime;
	var k; //数据下标
	nowTime = n;
	activityTime =g;
	k=x;
	console.log(nowTime);
	setInterval(function(){
		nowTime = parseInt(nowTime) + 1000;
		var now = nowTime; 
		var end1=parseInt(activityTime);//活动开始时间
		var end = end1+24*60*60*1000; //6048000000 活动持续时间
		var s=parseInt((end-now)/1000);//总秒数
		var d=parseInt(s/3600/24);
		var h=parseInt(s%(3600*24)/3600);
		if(h<10){
			h="0"+h;
		}
		var m=parseInt(s%3600/60);
		if(m<10){
			m="0"+m;
		}
		var s=s%60;
		if(s<10){
			s="0"+s;
		}
		//console.log($(".countdown"+k));
		$(".countdown"+k).html(h+":"+m+":"+s);
		//var timer1 = setTimeout('contdown_sub()',1000);
	//	if(end-now<=500){
	//		clearTimeout(timer1);
	//		mui.toast("若界面未刷新，请尝试手动刷新");
	//		location.reload();
	//		return;
	//	}
	},1000);
}
/*function contdown_sub(g,n,x){
	var nowTime;
	var activityTime;
	var k; //数据下标
	nowTime = n;
	activityTime =g;
	k=x;
	function daojishi(){
		nowTime = parseInt(nowTime) + 1000;
		var now = nowTime; 
		var end1=parseInt(activityTime);//活动开始时间
		var end = end1+24*60*60*1000; //6048000000 活动持续时间
		var s=parseInt((end-now)/1000);//总秒数
		var d=parseInt(s/3600/24);
		var h=parseInt(s%(3600*24)/3600);
		if(h<10){
			h="0"+h;
		}
		var m=parseInt(s%3600/60);
		if(m<10){
			m="0"+m;
		}
		var s=s%60;
		if(s<10){
			s="0"+s;
		}
		console.log($(".countdown"+k));
		console.log(h+":"+m+":"+s);
		$("countdown"+k).html(h+":"+m+":"+s);
			setTimeout('daojishi()',1000);
	}
daojishi();
//	setInterval('daojishi()',1000);
}*/
function number(){
	var num = $("#account").val();
	var beishu = Math.ceil( num / 500 );
	var numb = beishu *500;
	$("#account").val(numb);
}

//加载中
setTimeout(function(){
	$(".load,.showMask").css("display","none");
},1000);


function phone(){
	ajax({
		type: "post",
		url: "/app/customer/showPhone.do",
		data: {sessionId:sid},
		success: function(res) {
			if(res.status == 1) {
				$('.shoujihao').val(phoneHide(res.data));
			} else {
				mui.toast(res.msg);
			}
		}
	})
}
function phoneHide(phone){
	 var mphone = phone.substr(0, 3) + '****' + phone.substr(7);
	 return mphone;
}