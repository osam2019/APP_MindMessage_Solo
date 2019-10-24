<%@ page language="java" contentType="text/plain; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "java.sql.*" %>
<%
	String MN2 = request.getParameter("MN");
	int MN = Integer.parseInt(MN2);
	
	Class.forName("oracle.jdbc.OracleDriver");
	String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	String id = "scott";
	String pw = "1234";
	
	Connection db = DriverManager.getConnection(url, id, pw);
	
	String sql = "delete from message where messagenum = " +MN ;
	PreparedStatement pstmt = db.prepareStatement(sql);
	
	pstmt.execute();
	
	db.close();
%>