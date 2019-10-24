<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "java.sql.*"%>
<%@ page import = "org.json.simple.*"%>
<%
	Class.forName("oracle.jdbc.OracleDriver");
	String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	String id = "scott";
	String pw = "1234";
	
	Connection db = DriverManager.getConnection(url, id, pw);
	
	String t2 = request.getParameter("MN");
	int mn = Integer.parseInt(t2);
	String sql; 
	sql = "select * from reply where receiver = " + mn;
	
	PreparedStatement pstmt = db.prepareStatement(sql);
	ResultSet rs = pstmt.executeQuery();
	
	JSONArray root = new JSONArray();
	if (rs.next()) {
		String Sreplynum = rs.getString("replynum");
		String Stext_ = rs.getString("text_");
		int Sreceiver = rs.getInt("receiver");
		int Ssender = rs.getInt("sender");
		String Sname_ = rs.getString("name_");
		int Sstatus = rs.getInt("status");
		
		JSONObject obj = new JSONObject();
		obj.put("replynum", Sreplynum);
		obj.put("text_", Stext_);
		obj.put("receiver", Sreceiver);
		obj.put("sender", Ssender);
		obj.put("name_", Sname_);
		obj.put("status", Sstatus);
		root.add(obj);
	}
	db.close();

%>
<%= root.toJSONString() %>