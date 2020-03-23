<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title>充值</title>
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <script src="/js/jquery.min.js"></script>

</head>
<body>
<form id="form" action="/user/updateNickNameByUid.go" method="post">
    <input type="text" name="oldName" id="oldName" value="${user.name}"/>
    <span id="errorTxt"></span>
    <input type="button" value="修改" onclick="update()">
</form>
<script>

</script>
</body>
</html>