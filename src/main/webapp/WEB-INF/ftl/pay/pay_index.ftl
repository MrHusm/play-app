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
<#if userAccount??>
    ${userAccount.money?c}-${userAccount.virtualMoney?c}
</#if>
<input type="button" name="weixin" id="weixin" value="微信充值"/>&nbsp;&nbsp;&nbsp;
<input type="button" name="alipay" onclick="alipay()" id="alipay" value="支付宝充值"/>
<#if rechargeItems??>
<ul>
    <#list rechargeItems as rechargeItem>
        <li><input type="radio" name="rechargeItem" value="${rechargeItem.rechargeItemId}"/>${rechargeItem.price}-${rechargeItem.money}-${rechargeItem.virtual}</li>
    </#list>
</ul>
</#if>
<script>
    function alipay(){
        var rechargeItemId = $("input[name='rechargeItem']:checked").val();
        window.location.href = "/alipay/order.go?userId=1&type=1&productId="+rechargeItemId+"&param=${param}";
    }
</script>
</body>
</html>