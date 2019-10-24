<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "java.sql.*"%>
<%@ page import = "org.json.simple.*"%>
<%
	Class.forName("oracle.jdbc.OracleDriver");
	String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	String id = "scott";
	String pw = "1234";
	
	String s = request.getParameter("id");
	
	Connection db = DriverManager.getConnection(url, id, pw);
	
	String sql = "select id_, password_, idx, permission, name_ "
			+ "from infomation where id_ = '" + s +"'";
	
	PreparedStatement pstmt = db.prepareStatement(sql);
	ResultSet rs = pstmt.executeQuery();
	
	JSONArray root = new JSONArray();
	
	while(rs.next()) {
		String Sid = rs.getString("id_");
		String Spw = rs.getString("password_");
		int Sindex = rs.getInt("idx");
		String name = rs.getString("name_");
		int permission = rs.getInt("permission");
		
		JSONObject obj = new JSONObject();
		obj.put("id_", Sid);
		obj.put("pw_", Spw);
		obj.put("index_", Sindex);
		obj.put("permission", permission);
		obj.put("name_", name);
		
		root.add(obj);
	}
	db.close();
	
	

%>
<%= root.toJSONString() %>