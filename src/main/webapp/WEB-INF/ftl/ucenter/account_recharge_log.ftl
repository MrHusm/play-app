<#if syn=='0'>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>充值记录</title>
    <link rel="stylesheet" href="/css/reset_5.css">
    <link rel="stylesheet" href="/css/rechargeRecord.css">
</head>
<body>
<div class="pageLoad">
</#if>
<#if pageFinder.data??>
  <#list pageFinder.data as userAccountLog>
      <div class="rechargeList">
          <#if userAccountLog.type==1 || userAccountLog.type==2>
              <div>充值${userAccountLog.comment}元&nbsp;&nbsp;|&nbsp;&nbsp;${userAccountLog.unitMoney?c}钻<#if userAccountLog.unitVirtual gt 0>+${userAccountLog.unitVirtual?c}钻</#if></div>
          </#if>
          <#if userAccountLog.type==11>
              <div>客服赠送${userAccountLog.unitVirtual?c}钻</div>
          </#if>
          <#if userAccountLog.type==12>
              <div>签到赠送${userAccountLog.unitVirtual?c}钻</div>
          </#if>
          <#if userAccountLog.type==13>
              <div>新手礼包${userAccountLog.unitVirtual?c}钻</div>
          </#if>
          <time class="rechargeTime">${userAccountLog.createDate?string("yyyy-MM-dd HH:mm:ss")}</time>
      </div>
  </#list>
</#if>
<#if syn=='0'>
</div>
<div class="bookLoad" id="autopbn" curpage="${pageFinder.pageNo+1}" totalpage="${pageFinder.pageCount}" rel="/user/findUserAccountLog.go?token=${token}&page=${pageFinder.pageNo+1}&syn=1&type=${type}" style="display:none;"></div>

<script type="text/javascript" src="/js/base.js"></script>
<script type="text/javascript" src="/js/autopage.js"></script>
<script>

</script>
</body>
</html>
</#if>