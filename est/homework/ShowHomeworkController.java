package com.edu863.controller.controller012;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.edu863.authorization.UserPermission;
import com.edu863.authorization.UserPropEnum;
import com.edu863.bean.bean012.ShowHomeworkBean;
import com.edu863.bean.bean012.ShowHomeworkCommentBean;
import com.edu863.dao.common.DictionaryDao;
import com.edu863.dao.common.MessageDao;
import com.edu863.dao.common.SelectPropertyByKeyDao;
import com.edu863.dao.dao012.CommonDao012;
import com.edu863.dao.dao012.ShowHomeworkDao;
import com.edu863.service.common.DownloadFileService;
import com.edu863.service.service012.CommonService012;
import com.edu863.service.service012.ShowHomeworkService;

@Controller
@RequestMapping(value = "/homework/show/")
/**
 * module 作品展示与评价
 * class ShowHomeworkController.java
 * function showHomework画面控制器
 * author 唐义凡（刘垚）
 * remark
 */
public class ShowHomeworkController {
	// 自动装配ShowHomeworkService
	@Autowired
	private ShowHomeworkService showHomeworkService;
	// 自动装配DownloadFileService
	@Autowired
	private DownloadFileService downloadFileService;
	// 自动装配CommonService
	@Autowired
	private CommonService012 commonService;
	// 自动装配ShowHomeworkDao
	@Autowired
	private ShowHomeworkDao showHomeworkDao;
	// 自动装配CommonDao012
	@Autowired
	private CommonDao012 commonDao;
	// 自动装配DictionaryDao
	@Autowired
	private DictionaryDao dictionaryDao;
	// 自动装配MessageDao
	@Autowired
	private MessageDao messageDao;
	// 自动装配SelectPropertyByKeyDao
	@Autowired
	private SelectPropertyByKeyDao selectPropertyByKeyDao;
	// “非法访问”路径
	private final static String invalidAccess = "inc/invalidAccess";
	// 默认头像路径
	private static final String defaultFace = "001" + File.separatorChar + "img" + File.separatorChar + "default.jpg";

	@RequestMapping(value = "display")
	@UserPermission(userProp = { UserPropEnum.TEACHER, UserPropEnum.STUDENT })
	/**
	 * method display
	 * function 负责处理/homework/show/display的请求
	 * @param request 客户端发出的HTTP请求
	 * @param session HTTP会话
	 * @param model 生成画面所需的模型
	 * @return 画面路径
	 * @throws 无
	 */
	public String display(HttpServletRequest request, HttpSession session, Model model) {
		// 获取用户ID
		String userId = (String) session.getAttribute("userNO");
		// 获取学校编号
		String schoolId = (String) session.getAttribute("schoolNO");
		// 获取用户类型
		String userType = (String) session.getAttribute("userPROP");
		// 从HTTP请求中获取作品ID
		String homeworkId = request.getParameter("id");
		// 从HTTP请求中判断是否从HTTP会话中读取参数
		String isReturn = request.getParameter("return");
		if (!StringUtils.isEmpty(isReturn)) {
			model.addAttribute("commentText", session.getAttribute("commentText_012_007"));
			model.addAttribute("commentGrade", session.getAttribute("commentGrade_012_007"));
		}
		// 根据作品ID，获得作品和作者的信息
		ShowHomeworkBean bean = showHomeworkDao.getShowHomeworkInfo(homeworkId);
		// 如果没有检索到该作品
		if (StringUtils.isEmpty(bean.getWORKSID())) {
			return invalidAccess;
		}
		// 设置共通部分-画面ID
		model.addAttribute("pageID", "frm_homework_list_012_007");
		// 设置共通部分-画面名称
		model.addAttribute("pageName", "作品展示");
		// 将作品和作者的信息放入模型
		model.addAttribute("homeworkBean", bean);
		// 获取作品地址
		String homeworkAddress = commonDao.getHomeworkAddress(bean.getWORKSID());
		// 获取作品文件的扩展名
		String extension = homeworkAddress.substring(homeworkAddress.lastIndexOf('.') + 1).toLowerCase();
		if (commonService.isImg(homeworkAddress)) {
			// 如果作品是图片
			model.addAttribute("isImg", "1");
		} else if (extension.equals("swf")) {
			// 如果作品是flash
			model.addAttribute("swfPath", dictionaryDao.getDictionary("00016", "06") + homeworkAddress);
		} else {
			// 如果作品是其他文件
			model.addAttribute("isFile", "1");
		}
		// 根据作品ID，获得作品的评论列表
		List<ShowHomeworkCommentBean> commentList = showHomeworkDao.getShowHomeworkCommentList(schoolId, homeworkId, false);
		// 根据作品ID，获得作品的任课教师评论列表
		// （主讲）教师评分，这里教师的评分以最新评论为准。（暂时作品的综合评分是教师最新评分）
		List<ShowHomeworkCommentBean> teacherCommentList = showHomeworkDao.getShowHomeworkCommentList(schoolId, homeworkId, true);
		// 将评论放入模型
		model.addAttribute("commentList", commentList);
		// 将任课教师评论放入模型
		if (teacherCommentList.size() > 0) {
			model.addAttribute("teacherComment", teacherCommentList.get(teacherCommentList.size() - 1));
		}
		// 将错误消息放入模型
		model.addAttribute("W0120003", messageDao.getMessage("W0120003", null));
		model.addAttribute("W0120004", messageDao.getMessage("W0120004", new String[] { "评论" }));
		model.addAttribute("W0120005", messageDao.getMessage("W0120005", new String[] { "评论" }));
		model.addAttribute("commentTextW0010003", messageDao.getMessage("W0010003", new String[] { "评论内容" }));
		model.addAttribute("commentTextW0010006", messageDao.getMessage("W0010006", new String[] { "评论内容" }));
		model.addAttribute("commentGradeW0010006", messageDao.getMessage("W0010006", new String[] { "评价分数" }));
		model.addAttribute("deleteConfirmMessage", messageDao.getMessage("W0120001", new String[] { "删除" }));
		model.addAttribute("insertConfirmMessage", messageDao.getMessage("W0120001", new String[] { "评论" }));
		model.addAllAttributes(request.getParameterMap());
		// 将来源页面放入模型
		model.addAttribute("fromPage", request.getParameter("fromPage"));
		// 如果用户是班级的主讲教师
		if (userType.equals("03") && selectPropertyByKeyDao.getPropertyByKey("CLASSNO", bean.getCLASSID(), "TEACHERNO", "TBL_CLASS_001", "", schoolId).equals(userId)) {
			// 在模型中设置显示删除表单
			model.addAttribute("hasDeleteButton", "1");
		}
		return "012/showHomework";
	}

	@RequestMapping(value = "comment", method = RequestMethod.POST)
	@UserPermission(userProp = { UserPropEnum.TEACHER, UserPropEnum.STUDENT })
	/**
	 * method comment
	 * function 负责处理/homework/show/comment的请求
	 * @param request 客户端发出的HTTP请求
	 * @param session HTTP会话
	 * @param model 生成画面所需的模型
	 * @return 画面路径
	 * @throws 无
	 */
	public String comment(HttpServletRequest request, HttpSession session, Model model) {
		// 获取用户ID
		String userId = (String) session.getAttribute("userNO");
		// 获取用户类型
		String userType = (String) session.getAttribute("userPROP");
		// 获取学校编号
		String schoolId = (String) session.getAttribute("schoolNO");		
		// 从HTTP请求中获取学号
		String studentNo = request.getParameter("studentNo");
		// 从HTTP请求中获取班级编号
		String classId = request.getParameter("classId");
		// 从HTTP请求中获取作品ID
		String homeworkId = request.getParameter("homeworkId");
		// 从HTTP请求中获取评论内容
		String commentText = request.getParameter("commentText");
		// 从HTTP请求中获取评价分数
		String commentGrade = request.getParameter("commentGrade");
		// 在HTTP会话中保存评论内容
		session.setAttribute("commentText_012_007", commentText);
		// 在HTTP会话中保存评价分数
		session.setAttribute("commentGrade_012_007", commentGrade);
		// 将来源页面放入模型
		model.addAttribute("fromPage", request.getParameter("fromPage"));
		// 添加评论信息，获取错误消息编号，将错误消息编号放入模型
		model.addAllAttributes(showHomeworkService.insertComment(userId, userType, studentNo, classId, homeworkId, commentText, commentGrade, schoolId));
		// 返回画面路径
		return "redirect:/homework/show/display?return=0&id=" + homeworkId;
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@UserPermission(userProp = { UserPropEnum.TEACHER })
	/**
	 * method delete
	 * function 负责处理/homework/show/delete的请求
	 * @param request 客户端发出的HTTP请求
	 * @param session HTTP会话
	 * @param model 生成画面所需的模型
	 * @return 画面路径
	 * @throws 无
	 */
	public String delete(HttpServletRequest request, HttpSession session, Model model) {
		// 获取用户ID
		String userId = (String) session.getAttribute("userNO");
		// 获取学校编号
		String schoolId = (String) session.getAttribute("schoolNO");
		// 从HTTP请求中获取作品ID
		String homeworkId = request.getParameter("homeworkId");
		// 从HTTP请求中获取评论序号
		String commentId = request.getParameter("commentId");
		// 在HTTP会话中保存评论内容
		session.setAttribute("commentText_012_007", request.getParameter("commentText"));
		// 在HTTP会话中保存评价分数
		session.setAttribute("commentGrade_012_007", request.getParameter("commentGrade"));
		// 删除评论
		commonService.updateCommentValid(schoolId, userId, commentId, "0");
		// 将来源页面放入模型
		model.addAttribute("fromPage", request.getParameter("fromPage"));
		// 返回画面路径
		return "redirect:/homework/show/display?return=0&id=" + homeworkId;
	}

	@RequestMapping(value = "face")
	@UserPermission(userProp = { UserPropEnum.TEACHER, UserPropEnum.STUDENT })
	/**
	 * method open
	 * function 负责处理/homework/show/face的请求
	 * @param request 客户端发出的HTTP请求
	 * @param response 服务器回送给客户端的HTTP响应
	 * @return 无
	 * @throws 无
	 */
	public void face(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 头像路径
		String face = null;
		// 获取用户ID
		String id = request.getParameter("id");
		// 学校编号
		String schoolId = (String) session.getAttribute("schoolNO");
		// 获取用户类型
		String type = request.getParameter("type");
		if (!StringUtils.isEmpty(id) && !StringUtils.isEmpty(type)) {
			if ("teacher".equals(type)) {
				// 获取学生的头像路径
				face = selectPropertyByKeyDao.getPropertyByKey("TEACHERNO", id, "FACE", "TBL_TEACHER_001", "", schoolId);
			} else if ("student".equals(type)) {
				// 获取教师的头像路径
				face = selectPropertyByKeyDao.getPropertyByKey("STUDENTNO", id, "FACE", "TBL_STUDENT_001", "", schoolId);
			}
		}
		// 输出头像
		if (StringUtils.isEmpty(face)) {
			// 输出默认头像
			downloadFileService.open(response, dictionaryDao.getDictionary("00016", "01") + defaultFace);
		} else {
			// 输出用户头像
			face = face.replace('/', File.separatorChar);
			downloadFileService.open(response, dictionaryDao.getDictionary("00016", "01") + face);
		}
	}

	@RequestMapping(value = "back")
	@UserPermission(userProp = { UserPropEnum.TEACHER, UserPropEnum.STUDENT })
	/**
	 * method back
	 * function 负责处理/homework/show/back的请求
	 * @param request 客户端发出的HTTP请求
	 * @return 画面路径
	 * @throws 无
	 */
	public String back(HttpServletRequest request) {
		String fromPage = request.getParameter("fromPage");
		if (fromPage.equals("frm_homework_unaudit_012_002")) {
			return "redirect:/homework/audit/list?return=0";
		}
		return "redirect:/homework/list?search=1";
	}
}