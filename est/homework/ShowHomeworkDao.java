package com.edu863.dao.dao012;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.edu863.bean.bean012.ShowHomeworkBean;
import com.edu863.bean.bean012.ShowHomeworkCommentBean;
import com.edu863.dao.common.SelectPropertyByKeyDao;
import com.sun.istack.internal.FinalArrayList;

@Repository
/**
 * module 作品展示与评价
 * class ShowHomeworkDao.java
 * function showHomework画面持久层
 * author 唐义凡（刘垚）
 * remark
 */
public class ShowHomeworkDao {
	// 自动装配SelectPropertyByKeyDao
	@Autowired
	private SelectPropertyByKeyDao selectPropertyByKeyDao;
	// 自动装配JdbcTemplate
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	/**
	 * method getShowHomeworkInfo
	 * function 根据作品ID，获得作品和作者的信息
	 * @param homeworkId 作品ID
	 * @return 作品和作者的信息
	 * @throws 无
	 */
	public ShowHomeworkBean getShowHomeworkInfo(String homeworkId) {
		StringBuilder sql = new StringBuilder(" select WORKSID, WORKSNAME, CLASSID, TBL_STUDENT_001.STUDENTNO, FACE, NAME, TBL_WORKST_012_001.CRTDT ");
		sql.append(" from TBL_WORKST_012_001, TBL_STUDENT_001 ");
		// 匹配条件
		sql.append(" where TBL_WORKST_012_001.STUDENTNO = TBL_STUDENT_001.STUDENTNO ");
		// “作品ID”的条件
		sql.append(" and WORKSID = ? ");
		// “有效状态”的条件
		sql.append(" and TBL_STUDENT_001.ISVALID = ? ");
		sql.append(" and TBL_WORKST_012_001.ISVALID = ? ");
		// 执行select语句
		final ShowHomeworkBean bean = new ShowHomeworkBean();
		jdbcTemplate.query(sql.toString(), new String[] { homeworkId, "1", "1" }, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				bean.setWORKSID(rs.getString("WORKSID"));
				bean.setWORKSNAME(rs.getString("WORKSNAME"));
				bean.setSTUDENTNO(rs.getString("STUDENTNO"));
				bean.setNAME(rs.getString("NAME"));
				bean.setFACE(rs.getString("FACE"));
				bean.setCLASSID(rs.getString("CLASSID"));
				bean.setCRTDT(rs.getString("CRTDT"));
			}
		});
		// 返回作品和作者的信息
		return bean;
	}

	/**
	 * method getShowHomeworkCommentList
	 * function 根据作品ID，获得作品的评论信息列表
	 * @param homeworkId 作品ID
	 * @param isTeacher 是否任课教师
	 * @return 评论信息列表
	 * @throws 无
	 */
	public List<ShowHomeworkCommentBean> getShowHomeworkCommentList(String schoolId, String homeworkId, boolean isTeacher) {
		StringBuilder sql = new StringBuilder(" select COMMID, COMMDESC, COMTORID, COMMORDER, CRTDT, COMTORSTA, COMMDRESULT ");
		sql.append(" from TBL_WORKSCOMMT_012_002 ");
		final String schoolID = schoolId;
		// “作品ID”的条件
		sql.append(" where WORKSID = ? ");
		// “有效状态”的条件
		sql.append(" and ISVALID = ? ");
		// “是否通过审核”的条件
		sql.append(" and ISPASSED = ? ");
		// 任课教师的评论
		if (isTeacher) {
			sql.append(" and COMMORDER = ? ");
		} else {
			sql.append(" and COMMORDER > ? ");
		}
		// 排序
		sql.append(" order by CRTDT asc ");
		// 执行select语句
		final List<ShowHomeworkCommentBean> list = new ArrayList<ShowHomeworkCommentBean>();
		jdbcTemplate.query(sql.toString(), new String[] { homeworkId, "1", "02", "0" }, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				ShowHomeworkCommentBean bean = new ShowHomeworkCommentBean();
				bean.setCOMMID(rs.getString("COMMID"));
				bean.setCOMMDESC(rs.getString("COMMDESC"));
				bean.setCOMTORID(rs.getString("COMTORID"));
				bean.setCOMMORDER(rs.getString("COMMORDER"));
				bean.setCRTDT(rs.getString("CRTDT").substring(0, 19));
				bean.setCOMTORSTA(rs.getString("COMTORSTA"));
				bean.setCOMMDRESULT(rs.getString("COMMDRESULT"));
				// 获取姓名和头像
				if (bean.getCOMTORSTA().equals("03")) {
					// 如果教师的有效状态为有效
					if ("1".equals(selectPropertyByKeyDao.getPropertyByKey("TEACHERNO", bean.getCOMTORID(), "ISVALID", "TBL_TEACHER_001", "", schoolID))) {
						bean.setNAME(selectPropertyByKeyDao.getPropertyByKey("TEACHERNO", bean.getCOMTORID(), "NAME", "TBL_TEACHER_001", "", schoolID));
					}
				} else if (bean.getCOMTORSTA().equals("02")) {
					// 如果学生的有效状态为有效
					if ("1".equals(selectPropertyByKeyDao.getPropertyByKey("STUDENTNO", bean.getCOMTORID(), "ISVALID", "TBL_STUDENT_001", "", schoolID))) {
						bean.setNAME(selectPropertyByKeyDao.getPropertyByKey("STUDENTNO", bean.getCOMTORID(), "NAME", "TBL_STUDENT_001", "", schoolID));
					}
				}
				if (!StringUtils.isEmpty(bean.getNAME())) {
					list.add(bean);
				}
			}
		});
		// 返回评论信息列表
		return list;
	}

	/**
	 * method insertComment
	 * function 添加评论信息
	 * @param args 参数
	 * @return 无
	 * @throws 无
	 */
	public boolean insertComment(Object[] args) {
		StringBuilder sql = new StringBuilder(" insert into TBL_WORKSCOMMT_012_002 ");
		sql.append(" (WORKSID, COMMORDER, COMTORSTA, COMTORID, ISPASSED, COMMDESC, COMMDRESULT, SCHOOLNO, ISVALID, CRTDT, CRTUSID, UPDDT, UPDUSID) ");
		sql.append(" values (?, ?, ?, ?, ?, ?, ?, ?, '1', now(), ?, now(), ?) ");
		try {
			jdbcTemplate.update(sql.toString(), args);	
		} catch (Exception e) {
			return false;
		}
		return true;		
	}
	
	/**
	 * method insertComment
	 * function 添加评论信息
	 * @param args 参数
	 * @return 无
	 * @throws 无
	 */
	public boolean updateTeacherComment(final Object[] args) {
		StringBuilder sql = new StringBuilder(" update TBL_WORKST_012_001 ");
		sql.append(" set RATESCORE = ? , UPDUSID = ? , UPDDT = now() where WORKSID = ? and SCHOOLNO = ? and STUDENTNO = ? and TEACHERNO = ?");
		try {
			jdbcTemplate.update(sql.toString(), args);	
		} catch (Exception e) {
			return false;
		}		
		return true;		
	}

	/**
	 * method getTeacherComment
	 * function 获得主讲教师对作品的评论内容
	 * @param homeworkId 作品ID
	 * @return 评论内容
	 * @throws 无
	 */
	public String getTeacherComment(String homeworkId) {
		String sql = " select COMMDESC from TBL_WORKSCOMMT_012_002 where WORKSID = ? and COMMORDER = ? ";
		try {
			// 若存在则返回主讲教师编号
			return jdbcTemplate.queryForObject(sql, new String[] { homeworkId, "0" }, java.lang.String.class);
		} catch (Exception e) {
			// 若不存在则返回空字符串""
			return "";
		}
	}
}