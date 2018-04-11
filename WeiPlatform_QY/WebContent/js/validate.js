/**
 * 用于表单验证
 */
var param;
$.extend($.fn.validatebox.defaults.rules, {
    CHS: {
        validator: function (value, param) {
            return /^[\u0391-\uFFE5]+$/.test(value);
        },
        message: '请输入汉字'
    },
    /*
     * 验证输入的邮编是否正确
     */
    ZIP: {
        validator: function (value, param) {
            return /^[1-9]\d{5}$/.test(value);
        },
        message: '邮政编码不存在'
    },
    /*
     * 验证输入的手机号是否正确
     */
    mobile: {
        validator: function (value, param) {
        	//return /^((\+86)|(86))?(1)\d{10}$/.test(value);
        	return /(\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/.test(value);
            //return /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/.test(value);
        },
        message: '手机号码不正确'
    },
    minLength: {
        validator: function (value, param) {
            //return safePassword(value);
            return !(/^(|.{0,1})$|\s/.test(value));
        },
        message: 'ID为两位'
    },
    equalTo: {
        validator: function (value, param) {
            return value == $(param[0]).val();
        },
        message: '两次输入的字符不一至'
    },
    number: {
        validator: function (value, param) {
            return /^\d+$/.test(value);
        },
        message: '请输入数字'
    },
    
    /*
     * 验证输入的身份证号是否正确
     */
    idcard: {
        validator: function (value, param) {
            return idCard(value);
        },
        message:'请输入正确的身份证号码'
    },
    /*
     * 验证字符长度在..与..区间之内(销售范围)
     */
    lengthRange: {
    	validator: function (value, param){
    		if(value.length < param[0] || value.length > param[1]) {
        		return false;
        	} else {
        		return true;
        	}
    	},
    	message:'销售组织的ID为四位字符，请按规定重新输入！'
    },
    /*
     * 验证字符长度在..与..区间之内
     */
    length: {
    	validator: function (value, param){
    		if(value.length < param[0] || value.length > param[1]) {
        		return false;
        	} else {
        		return true;
        	}
    	},
    	message:'你输入的字符长度不合法，请按规定重新输入！'
    },
    /*
     *自定义手机号 
     */
    mobileNo: {
    	validator: function (value, param) {
        	//return /^((\+86)|(86))?(1)\d{10}$/.test(value);
        	//return /(\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/.test(value);
            //return /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/.test(value);
    		return /^1[358][0-9]{9}$/.test(value);
        },
        message: '手机号码不正确'
    }
    

});
