<html>
<head>
<title>File Uploading Form</title>
</head>
<body>
<h3>File Upload:</h3>
Select a file to upload: <br />
<form action="ImageUploadServlet" method="post"
                        enctype="multipart/form-data">
Slider ID: <input type="text" name="sliderId" size="50" />
<br>
Image : <input type="file" name="image" size="50" />
<br />
<input type="submit" value="Upload File" />
</form>
</body>
</html>