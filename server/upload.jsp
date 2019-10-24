<%@ page language="java" contentType="text/plain; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "java.sql.*" %>
<%
	Class.forName("oracle.jdbc.OracleDriver");
	String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	String id = "scott";
	String pw = "1234";
	
	String id_ = request.getParameter("id_");
	String text = request.getParameter("text_");
	String type_ = request.getParameter("type_");
	String an = request.getParameter("an");
	
	Connection db = DriverManager.getConnection(url, id, pw);
	PreparedStatement pstmt;
	String sql = "insert into message"
			+ "(idx, name, type_, messagenum, text_, status)"
			+ " values (?, ?, ?, MN.nextval, ?, 0)";
	
	
	String sql2 = "select name_, idx from infomation where id_ = '" + id_ + "'";

	PreparedStatement pstmt2 = db.prepareStatement(sql2);
	ResultSet rs = pstmt2.executeQuery();
	rs.next();
	String name_ = rs.getString("name_");
	int ii = rs.getInt("idx");
	
	pstmt = db.prepareStatement(sql);
	pstmt.setInt(1, ii);
	if (!(an.equals("anonymous"))) {
		pstmt.setString(2, name_);
	}
	else {
		pstmt.setString(2, "anonymous");
	}
	pstmt.setString(3, type_);
	pstmt.setString(4, text);
	
	if (type_ != null) {
		pstmt.execute();
	}
	
	db.close();

%>