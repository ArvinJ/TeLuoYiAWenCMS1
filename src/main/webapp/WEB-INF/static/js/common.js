/**
 * Created by lgq on 2015/8/27.
 */


function addInput(form,name,value,type) {
    //防止设置form的属性
    if (form[name] != null && (typeof form[name]) != "string") {
        form[name].value = value;
    } else {
        if (null == type)
            type = "hidden";
        var input = document.createElement('input');
        input.setAttribute("name", name);
        input.setAttribute("value", value);
        input.setAttribute("type", type);
        form.appendChild(input);
    }
}


function addHiddens(form,paramSeq){
    var params = paramSeq.split("&");
    for(var i=0;i<params.length;i++){
        if(params[i]!=""){
            var name = params[i].substr(0,params[i].indexOf("="));
            var value =params[i].substr(params[i].indexOf("=")+1);
            addInput(form,name,value,"hidden");
        }
    }
}
function addParamsInput(form,value){
    addInput(form,"params",value,"hidden");
}
function transferParams(from ,to,prefix,getEmpty){
    if(getEmpty==null)
        getEmpty=true;
    var params = getInputParams(from,prefix,getEmpty);
    addHiddens(to,params);
}


function getInputParams(form,prefix,getEmpty){
    var elems = form.elements;
    var params = "";
    if(null==getEmpty) getEmpty=true;

    for(i =0;i < elems.length; i++){
        if(""!=elems[i].name){
            if("params"==elems[i].name) continue;
            //alert(elems[i].tagName+":"+elems[i].value);
            if((elems[i].value=="")&&(getEmpty==false)) continue;
            if(null!=prefix){
                if(elems[i].name.indexOf(prefix)==0&&elems[i].name.indexOf(".")>1){
                    if((elems[i].type=="radio" ||elems[i].type=="checkbox")&& elems[i].checked==false)
                        continue;
                    if(elems[i].value.indexOf('&')!=-1){
                        params+="&" + elems[i].name + "=" + escape(elems[i].value);
                    }else{
                        params+="&" + elems[i].name + "=" + elems[i].value;
                    }
                }
            }
            else{
                if((elems[i].type=="radio" ||elems[i].type=="checkbox")&& elems[i].checked==false)
                    continue;
                if(elems[i].value.indexOf('&')!=-1){
                    params+="&" + elems[i].name + "=" + escape(elems[i].value);
                }else{
                    params+="&" + elems[i].name + "=" + elems[i].value;
                }
            }
        }
    }
    //alert("[getInputParams]:"+params);
    return params;
}
/**
 * 设定选择框中的选择项(单项)
 */
function setSelected(select,id){
    //alert("setSelected:option:"+id);
    for(var i=0;i<select.options.length;i++){
        if(select.options[i].value==id){
            select.options[i].selected=true;
            //alert("get:"+select.options[i].value)
            break;
        }
    }
}
/**
 * 设定选择框中的选择项(多项)
 */
function setSelectedSeq(select,idSeq){
    if(idSeq.indexOf(',')!=0){
        idSeq=","+idSeq;
    }
    if(idSeq.lastIndexOf(',')!=idSeq.length-1){
        idSeq=idSeq+",";
    }
    for(var i=0;i<select.options.length;i++){
        if(idSeq.indexOf(','+select.options[i].value+',')!=-1)
            select.options[i].selected=true;
    }
}

//获取select所有option的值
function getAllOptionValue(select)
{
    var val = "";
    var options = select.options;
    for (var i=0; i<options.length; i++)
    {
        if (val != "")
            val = val + ",";
        val = val + options[i].value;
    }
    return val;
}
//获取select选中的option的值
function getSelectedOptionValue(select)
{
    var val = "";
    var options = select.options;
    for (var i=0; i<options.length; i++)
    {
        if (options[i].selected)
        {
            if (val != "")
                val = val + ",";
            val = val + options[i].value;
        }
    }
    return val;
}


function selectedCheckBoxContents(cb_name) {
    try {
        var cb = document.getElementsByName(cb_name);
        var yCount = selectedCheckBoxLength(cb_name);

        var yContent = new Array(yCount);
        for (var i = 0; i < yCount; i++) {
            for (var j = i; j < cb.length; j ++) {
                if (cb[j].checked) {
                    yContent[i] = cb[j].value;
                    break;
                }
            }
        }
        return yContent;
    } catch (e) {
        return null;
    }
}

function selectedCheckBoxLength(cb_name) {

    try {
        var cb = document.getElementsByName(cb_name);
        var yCount = 0;
        for (var i = 0; i < cb.length; i++) {
            if (cb[i].checked) {
                yCount ++;
            }
        }
        return yCount;
    } catch (e) {
        return 0;
    }
}


function submitId(form,id,isMulti,action,promptMsg){
    var selectId = getSelectIds(id);
    if(null==isMulti)
        isMulti=false;
    if(""==selectId){
        alert(isMulti?"请选择一个或多个进行操作":"请选择一个进行操作");
        return;
    }
    if(!isMulti&&isMultiId(selectId)){
        alert("请仅选择一个进行操作");
        return;
    }
    if(null!=action){
        form.action=action;
    }
    if(isMulti){
        addInput(form,id+'s',selectId,"hidden");
    }else{
        addInput(form,id,selectId,"hidden");
    }
    if(null!=promptMsg){
        if(!confirm(promptMsg))return;
    }
    form.submit();
}

/**
 * 返回多选列表中选择的值<br>
 * @return 多个值以,相隔.没有选中时,返回""
 */
function getSelectIds(checkBoxName){
    var tmpIds=getCheckBoxValue(document.getElementsByName(checkBoxName));
    if(tmpIds==null){
        return "";
    }
    else{
        return tmpIds;
    }
}
/**
 * 返回单选列表中选择的值<br>
 * @return 没有选中时,返回""
 */
function getSelectId(radioBoxName){
    return getRadioValue(document.getElementsByName(radioBoxName))
}
function getSelectValues(obj){
    var tmpValues = "";
    if (null == obj) {
        return "";
    }
    for(var i = 0; i < obj.options.length; i++) {
        tmpValues += obj.options[i].value + ",";
    }
    return tmpValues;
}

function getRadioValue(radio){
    return boxAction(radio, "value");
}

function getCheckBoxValue(chk){
    return boxAction(chk, "value");
}

function boxAction(box, action,event){
    var val = "";
    if (box){
        if (! box[0]){
            if (action == "selected"){
                return box.checked;
            } else if (action == "value"){
                if (box.checked)
                    val = box.value;
            } else if (action == "toggle"){
                var srcElement = getEventTarget(event);
                box.checked = srcElement.checked;
                if(typeof box.onchange =="function"){
                    box.onchange();
                }
            }
        } else{
            for (var i=0; i<box.length; i++){
                if (action == "selected"){
                    if (box[i].checked)
                        return box[i].checked;
                } else if (action == "value"){
                    if (box[i].checked){
                        if (box[i].type == "radio"){
                            val = box[i].value;
                        } else if (box[i].type == "checkbox"){
                            if (val != "")
                                val = val + ",";
                            val = val + box[i].value;
                        }
                    }
                } else if (action == "toggle"){
                    var srcElement = getEventTarget(event);
                    box[i].checked = srcElement.checked;
                    if(typeof box[i].onchange =="function"){
                        box[i].onchange();
                    }
                }
            }
        }
    }

    if (action == "selected")
        return false;
    else
        return val;
}


function isMultiId(str)
{
    return str.indexOf(",")>0;
}


//查询TextArea字符长度，长度可自定义,默认200
function checkTextLength(textAreaContent, displayTitle, maxLength) {
    if (maxLength == null || maxLength == "") {
        maxLength = 200;
    }
    if (getStringLength(textAreaContent, maxLength) > maxLength) {
        alert(displayTitle + "不能超过" + maxLength + "个字符！");
        return false;
    }
    return true;
}


//计算字符串的字节数长度
function getStringLength(str, pointLength) {
    if (str == null || str == "") {
        return 0;
    }
    var strLen = 0;
    for (var i = 0; i < str.length; i ++) {
        if (Math.abs(str.charCodeAt(i)) <= 255) {
            strLen ++;
        } else {
            strLen += 2;
        }
    }
    return strLen;
}

//去除字符串的所有空格
function cleanSpaces(str) {
    if (str == null || str == "") {
        return str;
    }

    return str.replace(" ", "");
}
