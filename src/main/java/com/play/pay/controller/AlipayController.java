package com.play.pay.controller;

import com.alipay.api.internal.util.AlipaySignature;
import com.play.base.controller.BaseController;
import com.play.base.exception.ServiceException;
import com.play.base.utils.ResultResponse;
import com.play.pay.config.AlipayConfig;
import com.play.pay.model.AlipayOrder;
import com.play.pay.model.AlipayResponse;
import com.play.pay.service.IAlipayOrderService;
import com.play.pay.service.IAlipayResponseService;
import com.play.pay.service.IRechargeItemService;
import com.play.ucenter.service.IUserAccountService;
import com.play.ucenter.service.IUserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@Scope("prototype")
@RequestMapping("alipay")
public class AlipayController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AlipayController.class);

    @Resource(name = "vipService")
    IVipService vipService;

    @Resource(name = "userService")
    IUserService userService;

    @Resource(name = "userAccountService")
    IUserAccountService userAccountService;

    @Resource(name = "userAccountLogService")
    IUserAccountLogService userAccountLogService;

    @Resource(name = "userVipService")
    IUserVipService userVipService;

    @Resource(name = "alipayOrderService")
    IAlipayOrderService alipayOrderService;

    @Resource(name = "alipayResponseService")
    IAlipayResponseService alipayResponseService;

    @Resource(name = "rechargeItemService")
    IRechargeItemService rechargeItemService;

    /**
     * 转账
     *
     * @param productId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/order")
    public ResultResponse transferAccount(@RequestParam(required = true) Integer productId) throws ServiceException {
        Long userId = this.getUserId();
        alipayOrderService.order(userId, productId);
        return resultResponse.success();
    }


    /**
     * 支付后异步通知接口，完成充值
     *
     * @param response
     * @param request
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, value = "transactionManager")
    @RequestMapping("notifyUrl")
    public void notifyUrl(HttpServletResponse response, HttpServletRequest request) {
        try {
            //获取支付宝POST过来反馈信息
            Map<String, String> params = new HashMap<String, String>();
            Map requestParams = request.getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
                //乱码解决，这段代码在出现乱码时使用。
                //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
                params.put(name, valueStr);
            }
            //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
            //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
            boolean flag = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE);

            if (flag) {//验证成功
                //////////////////////////////////////////////////////////////////////////////////////////
                //请在这里加上商户的业务逻辑程序代码
                logger.info("notifyUrl_:验证成功");
                //商户订单号
                String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
                //支付宝交易号
                String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
                //交易状态
                String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
                //订单金额（元）
                String total_amount = null;
                if (StringUtils.isNotBlank(request.getParameter("total_amount"))) {
                    total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
                }
                //实收金额（元）
                String receipt_amount = null;
                if (StringUtils.isNotBlank(request.getParameter("receipt_amount"))) {
                    receipt_amount = new String(request.getParameter("receipt_amount").getBytes("ISO-8859-1"), "UTF-8");
                }

                //保存或修改response表
                AlipayResponse alipayResponse = alipayResponseService.findUniqueByParams("outTradeNo", out_trade_no);
                if (alipayResponse == null) {
                    alipayResponse = new AlipayResponse();
                }
                //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
                if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
                    if (alipayResponse.getStatus() == null || alipayResponse.getStatus() != 1) {
                        //支付成功充值
                        AlipayOrder order = this.alipayOrderService.findUniqueByParams("WIDoutTradeNo", out_trade_no);
                        //充值
                        this.userService.charge(order.getUserId(), 1, order.getChannel(), out_trade_no, order.getProductId());
                        alipayResponse.setStatus(1);
                    }
                } else {
                    alipayResponse.setStatus(0);
                }
                alipayResponse.setOutTradeNo(out_trade_no);
                alipayResponse.setReceiptAmount(StringUtils.isBlank(receipt_amount) ? null : Double.parseDouble(receipt_amount));
                alipayResponse.setTotalAmount(StringUtils.isBlank(total_amount) ? null : Double.parseDouble(total_amount));
                alipayResponse.setTradeNo(trade_no);
                alipayResponse.setTradeStatus(trade_status);
                alipayResponse.setUpdateDate(new Date());
                if (alipayResponse.getAlipayResponseId() != null) {
                    alipayResponseService.update(alipayResponse);
                } else {
                    alipayResponse.setCreateDate(new Date());
                    alipayResponseService.save(alipayResponse);
                }

                //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
                out("success", response);    //请不要修改或删除

                //////////////////////////////////////////////////////////////////////////////////////////
            } else {//验证失败
                out("fail", response);
            }
        } catch (Exception e) {
            out("fail", response);
            e.printStackTrace();
        }
    }

    private void out(String content, HttpServletResponse response) {
        try {
            ServletOutputStream out = response.getOutputStream();
            out.write(content.getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
