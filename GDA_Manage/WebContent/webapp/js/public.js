//获取验证码
$('.dxyz').on('click', function(e) {
	e.preventDefault();
	var tar = $(e.target);
	console.log(tar);
	var sj = $('.shoujihao').val();
	if(sj != '') {
		if(sj.length == 11) {
			tar.attr("disabled", true);
			var h = 120;
			var tem = setInterval(function() {
				if(h > 0) {
					tar.text(h);
					h--;
				} else {
					clearInterval(tem);
					tem = null;
					tar.attr('disabled', false);
					tar.text('重新获取');
				}
			}, 1000);
			console.log('sss');
			$.post(base + '/app/customer/getRegisterAuthCode.do', {
				phone: sj
			}, function(res) {
				console.log(res);
				if(res.status != 1) {
					mui.toast(res.msg);
				}
			})
		} else {
			mui.toast('请输入正确的手机号')
		}
	} else {
		mui.toast('请输入手机号');
	}
});
$('.dxyzs').on('click', function(e) {
	e.preventDefault();
	var tar = $(e.target);
	console.log(tar);
	tar.attr("disabled", true);

	var h = 120;
	var tem = setInterval(function() {
		if(h > 0) {
			tar.text(h);
			h--;
		} else {
			clearInterval(tem);
			tem = null;
			tar.attr('disabled', false);
			tar.text('重新获取');
		}
	}, 1000);
	var id = localStorage['sessionId'];
	console.log(id);
	$.post(base + '/app/customer/getAuthCode.do?sessionId='+id, function(res) {
		console.log(res);
		if(res.status != 1) {
			mui.toast(res.msg);
		}
	})

});
$('.content').off('click').on('click', '[data-padid]', function() {
	//console.log(2);
	$(this).addClass('active').siblings().removeClass('active');
	var id = $(this).attr('data-padid');
	$(id).addClass('active').siblings().removeClass('active');

});

$(document).ready(function() {
	$('.tanchuang').click(function(event) {
		if(event.target == this) {
			$(this).css('display', 'none');
		}
	})
})