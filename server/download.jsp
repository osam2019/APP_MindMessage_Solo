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
	
	String t2 = request.getParameter("t");
	int t = Integer.parseInt(t2);
	String sql;
	if (t == -1) {
		sql = "select * "
				+ "from message";
	}
	else {
		sql = "select * "
				+ "from message where messagenum = " + t;
	}
	
	
	
	PreparedStatement pstmt = db.prepareStatement(sql);
	ResultSet rs = pstmt.executeQuery();
	
	JSONArray root = new JSONArray();
	
	while(rs.next()) {
		String Stype = rs.getString("type_");
		String Sname = rs.getString("name");
		int Sindex = rs.getInt("idx");
		String Stext = rs.getString("text_");
		int SMN = rs.getInt("messagenum");
		int Sstatus = rs.getInt("status");
		
		JSONObject obj = new JSONObject();
		obj.put("type_", Stype);
		obj.put("name", Sname);
		obj.put("idx", Sindex);
		obj.put("text_", Stext);
		obj.put("messagenum", SMN);
		obj.put("status", Sstatus);
		root.add(obj);
	}
	db.close();

%>
<%= root.toJSONString() %>