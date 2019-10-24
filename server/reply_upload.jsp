<%@ page language="java" contentType="text/plain; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "java.sql.*" %>
<%
	Class.forName("oracle.jdbc.OracleDriver");
	String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	String id = "scott";
	String pw = "1234";
	
	String id_ = request.getParameter("id_");
	String text_ = request.getParameter("text_");
	String receiver = request.getParameter("MN");
	int rr = Integer.parseInt(receiver);
	
	Connection db = DriverManager.getConnection(url, id, pw);
	PreparedStatement pstmt;
	String sql = "insert into reply"
			+ "(replynum, text_, receiver, sender, name_, status)"
			+ " values (re_id.nextval, ?, ?, ?, ?, 0)";
	
	
	String sql2 = "select name_, idx from infomation where id_ = '" + id_ + "'";
	
	
	
	

	PreparedStatement pstmt2 = db.prepareStatement(sql2);
	ResultSet rs = pstmt2.executeQuery();
	rs.next();
	String name_ = rs.getString("name_");
	int ii = rs.getInt("idx");
	
	pstmt = db.prepareStatement(sql);
	pstmt.setInt(3, ii);
	pstmt.setString(4, name_);

	pstmt.setString(1, text_);
	pstmt.setInt(2, rr);
	
	pstmt.execute();
	
	db.close();
%>