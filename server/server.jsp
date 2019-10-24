<%@ page language="java" contentType="text/plain; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import = "java.sql.*" %>
<%
	String s = request.getParameter("id");
	String s2 = request.getParameter("pw");
	String s3 = request.getParameter("name");
	
	Class.forName("oracle.jdbc.OracleDriver");
	String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	String id = "scott";
	String pw = "1234";
	
	Connection db = DriverManager.getConnection(url, id, pw);
	
	String sql = "insert into infomation"
			+ "(idx, id_, password_, name_, permission)"
			+ " values (mobile_seq.nextval, ?, ?, ?, 0)";
	PreparedStatement pstmt = db.prepareStatement(sql);
	pstmt.setString(1, s);
	pstmt.setString(2, s2);
	pstmt.setString(3, s3);
	
	if (!(s == null || s2 == null || s3 == null)) {
		pstmt.execute();
	}
	
	db.close();
%>