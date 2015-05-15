jQuery(function($) {
	// 设置图片最大宽度
	var imgWidth = $("#imgWidth").width();
	$(".imgQuestion").css("max-width", imgWidth);
	// 设置focus
	$("#testpaperName").focus();
	if ($("#testpaperNameW0010003").text() || $("#testpaperNameW0010006").text()) {
		$("#testpaperName").focus();
	} else {
		for (i = 1; i <= 6; i++) {
			if ($("#questionScore" + i + "W0010006").text()) {
				$("#questionScore" + i).focus();
				break;
			}
			if ($("#questionScore" + i + "W0010008").text()) {
				$("#questionScore" + i).focus();
				break;
			}
			if ($("#questionScore" + i + "W0010017").text()) {
				$("#questionScore" + i).focus();
				break;
			}
		}
	}
	// 设置disabled
	if ($("#readOnly").val()) {
		// 试卷名字
		$("#testpaperName").attr("disabled", "disabled");
		// 分值
		$(".questionScore").attr("disabled", "disabled");
		// 按键
		$("#disrupt").attr("disabled", "disabled");
		$("#insert").attr("disabled", "disabled");
		$("#update").attr("disabled", "disabled");
		$("#delete").attr("disabled", "disabled");
		// 复选框
		$("[name='datacheck']").attr("disabled", "disabled");
		$("#back").focus();
	}
	// 试卷使用中
	if ($("#hasBeenUsingMessage").val()) {
		alert($("#hasBeenUsingMessage").val());
	}
	// “乱序”的消息
	if ($("#disruptSuccessMessage").val()) {
		alert($("#disruptSuccessMessage").val());
	}
	if ($("#disruptFailedMessage").val()) {
		alert($("#disruptFailedMessage").val());
	}	
	// “更新题目顺序”的消息
	if ($("#modIndexSuccessMessage").val()) {
		alert($("#modIndexSuccessMessage").val());
	}
	if ($("#modIndexFailedMessage").val()) {
		alert($("#modIndexFailedMessage").val());
	}
	// “保存输入”的消息
	if ($("#updateSuccessMessage").val()) {
		alert($("#updateSuccessMessage").val());
	}
	if ($("#updateFailedMessage").val()) {
		alert($("#updateFailedMessage").val());
	}
	// “删除”的消息
	if ($("#deleteSuccessMessage").val()) {
		alert($("#deleteSuccessMessage").val());
	}
	if ($("#deleteFailedMessage").val()) {
		alert($("#deleteFailedMessage").val());
	}
	// 乱序
	$("#disrupt").click(function() {
		if (confirm($("#disruptConfirmMessage").val())) {
			$("#show").attr("action", "show/disrupt");
			$("#show").submit();
		}
	});
	// 追加
	$("#insert").click(function() {
		$("#show").attr("action", "show/insert");
		$("#show").submit();
	});
	// 保存输入
	$("#update").click(function() {
		if (confirm($("#updateConfirmMessage").val())) {
			$("#show").attr("action", "show/update");
			$("#show").submit();
		}
	});
	// 返回
	$("#back").click(function() {
		if (confirm($("#backConfirmMessage").val())) {
			$("#show").attr("action", "show/back");
			$("#show").submit();
		}
	});
	// 与下题交换顺序
	$(".questionIndex").click(function() {
		var qsid = jQuery(this).attr("qsid");
		var qsindex = jQuery(this).attr("qsindex");
		var qstype = jQuery(this).attr("qstype");
		$("#qsid").val(qsid);
		$("#qsindex").val(qsindex);
		$("#qstype").val(qstype);
		$("#show").attr("action", "show/modIndex");
		$("#show").submit();
	});
});