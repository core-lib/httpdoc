/**
 * 判断字符串是否以某字符串开头
 * @param prefix 前缀
 * @returns {boolean} true: 是， false: 不是
 */
String.prototype.startsWith = function (prefix) {
    return this.length >= prefix.length && this.substring(0, prefix.length) === prefix;
};

/**
 * 判断字符串是否以某字符串结尾
 * @param suffix 后缀
 * @returns {boolean} true: 是， false: 不是
 */
String.prototype.endsWith = function (suffix) {
    return this.length >= suffix.length && this.substring(this.length - suffix.length) === suffix;
};

/**
 * 字符串前后trim
 * @returns {string} trim后的字符串
 */
String.prototype.trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, '');
};

/**
 * 日期格式化
 * @param pattern 格式化模式
 * @returns {*} 格式化后的字符串
 */
Date.prototype.format = function (pattern) {
    var o = {
        "M+": this.getMonth() + 1,                      //月份
        "d+": this.getDate(),                           //日
        "H+": this.getHours(),                          //小时
        "m+": this.getMinutes(),                        //分
        "s+": this.getSeconds(),                        //秒
        "q+": Math.floor((this.getMonth() + 3) / 3),    //季度
        "S": this.getMilliseconds()                     //毫秒
    };
    if (/(y+)/.test(pattern)) pattern = pattern.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o) if (new RegExp("(" + k + ")").test(pattern)) pattern = pattern.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return pattern;
};

/**
 * XML转换器
 * @constructor
 */
function XMLConversion() {

    this.stringify = function (obj, name) {
        name = name ? name : "xml";
        var xml = "";
        var type = $.isArray(obj) ? "array" : typeof obj;
        switch (type) {
            case 'boolean':
                xml += "<" + name + ">";
                xml += obj;
                xml += "</" + name + ">";
                break;
            case 'number':
                xml += "<" + name + ">";
                xml += obj;
                xml += "</" + name + ">";
                break;
            case 'string':
                xml += "<" + name + ">";
                xml += "<![CDATA[" + obj + "]]>";
                xml += "</" + name + ">";
                break;
            case 'array':
                xml += "<" + name + ">";
                for (var i = 0; obj && i < obj.length; i++) xml += this.stringify(obj[i], name);
                xml += "</" + name + ">";
                break;
            case 'object': {
                xml += "<" + name + ">";
                for (var k in obj) xml += this.stringify(obj[k], k);
                xml += "</" + name + ">";
            }
                break;
            default:
                return xml;
        }
        return xml;
    };

    this.parse = function (xml) {
        return objTree.parseXML(xml);
    };

    this.beautify = function (xml) {
        return formatXml(xml);
    };

    return this;
}

window.XMLConverter = new XMLConversion();

/**
 * JSON转换器
 * @constructor
 */
function JSONConversion() {

    this.stringify = function (obj) {
        return JSON.stringify(obj);
    };

    this.parse = function (json) {
        return JSON.parse(json);
    };

    this.beautify = function (json) {
        var formatted = '',     //转换后的json字符串
            padIdx = 0,         //换行后是否增减PADDING的标识
            PADDING = '    ';   //4个空格符
        /**
         * 将对象转化为string
         */
        if (typeof json !== 'string') {
            json = JSON.stringify(json);
        }
        /**
         *利用正则类似将{'name':'ccy','age':18,'info':['address':'wuhan','interest':'playCards']}
         *---> \r\n{\r\n'name':'ccy',\r\n'age':18,\r\n
         *'info':\r\n[\r\n'address':'wuhan',\r\n'interest':'playCards'\r\n]\r\n}\r\n
         */
        json = json.replace(/[\r\n]/g, '').replace(/[ ]/g, '')
            .replace(/([{])/g, '$1\r\n')
            .replace(/([}])/g, '\r\n$1\r\n')
            .replace(/([\[])/g, '$1\r\n')
            .replace(/([\]])/g, '\r\n$1\r\n')
            .replace(/(,)/g, '$1\r\n')
            .replace(/(\r\n\r\n)/g, '\r\n')
            .replace(/\r\n,/g, ',').trim();
        /**
         * 根据split生成数据进行遍历，一行行判断是否增减PADDING
         */
        (json.split('\r\n')).forEach(function (node) {
            var indent = 0, padding = '';
            if (node.match(/{$/) || node.match(/\[$/)) indent = 1;
            else if (node.match(/}/) || node.match(/]/)) padIdx = padIdx !== 0 ? --padIdx : padIdx;
            else indent = 0;
            for (var i = 0; i < padIdx; i++) padding += PADDING;
            formatted += padding + node + '\r\n';
            padIdx += indent;
        });
        return formatted;
    };

    return this;
}

window.JSONConverter = new JSONConversion();

window.objTree = new ObjTree();
window.jklDump = new JKL.Dumper();


/**
 * HttpDoc 框架
 */
function HttpDoc() {
    var DOC = {};
    var MAP = {};
    var REF_PREFIX = "$/schemas/";
    var REF_SUFFIX = "";
    var MAP_PREFIX = "Dictionary<String,";
    var MAP_SUFFIX = ">";
    var ARR_PREFIX = "";
    var ARR_SUFFIX = "[]";
    var INDENT = "    ";
    var SETTING = {};
    var DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    this.explore = function () {
        var self = this;
        var httpdocURL = $("#httpdoc-url").val();

        $.ajax({
            url: httpdocURL,
            data: {
                "format.canonical": true
            },
            method: "GET",
            beforeSend: function () {
                $("#httpdoc-loading").modal('show');
            },
            success: function (doc) {
                try {
                    doc = typeof doc === 'object' ? doc : JSON.parse(doc);
                    doc.url = httpdocURL;
                    self.init(doc);
                    $("#httpdoc-body").show();
                } catch (e) {
                    $("#httpdoc-body").hide();
                    var tpl = $("#tpl-httpdoc-error").html();
                    var html = Mustache.render(tpl, {
                        code: -1,
                        message: "Unknown Error",
                        body: doc,
                        url: httpdocURL
                    });
                    $("#httpdoc-head").html(html);
                }
            },
            error: function (xhr) {
                $("#httpdoc-body").hide();

                var tpl = $("#tpl-httpdoc-error").html();
                var html = Mustache.render(tpl, {
                    code: xhr.status,
                    message: xhr.statusText,
                    body: xhr.responseText,
                    url: httpdocURL
                });
                $("#httpdoc-head").html(html);

            },
            complete: function () {
                $("#httpdoc-loading").modal('hide');
            }
        });
    };

    this.init = function (doc) {
        DOC = doc;

        DOC.controllers = DOC.controllers ? DOC.controllers : [];

        // 给对象取一个唯一标识
        var id = 0;
        DOC.controllers.forEach(function (controller) {
            /** @namespace controller.operations */
            if (!controller.operations) return;
            controller.id = id++;
            controller.operations.forEach(function (operation) {
                operation.id = id++;
            });
        });

        // 补全Operation的注释信息，避免Mustache渲染时取了Controller的注释
        DOC.controllers.forEach(function (controller) {
            if (!controller.operations) return;
            controller.operations.forEach(function (operation) {
                operation.summary = operation.summary ? operation.summary : "";
                operation.description = operation.description ? operation.description : "";
                operation.deprecated = operation.deprecated ? operation.deprecated : "";
            });
        });

        // 补全Parameter的path信息，避免Mustache渲染时取了Operation的path
        DOC.controllers.forEach(function (controller) {
            if (!controller.operations) return;
            controller.operations.forEach(function (operation) {
                if (!operation.parameters) return;
                operation.parameters.forEach(function (parameter) {
                    parameter.path = parameter.path ? parameter.path : "";
                });
            });
        });

        // 补全Operation的path信息，方便Mustache渲染时取了Controller的path
        DOC.controllers.forEach(function (controller) {
            if (!controller.operations) return;
            controller.operations.forEach(function (operation) {
                var cPath = controller.path;
                var oPath = operation.path;
                operation.path = "" + (cPath ? cPath : "") + (oPath ? oPath : "");
            });
        });

        MAP = {};
        DOC.controllers.forEach(function (controller) {
            controller.tags = controller.tags ? controller.tags : [controller.name];
            controller.tags.forEach(function (tag) {
                var controllers = MAP[tag];
                if (controllers) {
                    controllers.push(controller);
                } else {
                    MAP[tag] = [controller];
                }
            });
        });

        /** @namespace doc.refPrefix */
        REF_PREFIX = doc.refPrefix ? doc.refPrefix : REF_PREFIX;
        /** @namespace doc.refSuffix */
        REF_SUFFIX = doc.refSuffix ? doc.refSuffix : REF_SUFFIX;
        /** @namespace doc.mapPrefix */
        MAP_PREFIX = doc.mapPrefix ? doc.mapPrefix : MAP_PREFIX;
        /** @namespace doc.mapSuffix */
        MAP_SUFFIX = doc.mapSuffix ? doc.mapSuffix : MAP_SUFFIX;
        /** @namespace doc.arrPrefix */
        ARR_PREFIX = doc.arrPrefix ? doc.arrPrefix : ARR_PREFIX;
        /** @namespace doc.arrSuffix */
        ARR_SUFFIX = doc.arrSuffix ? doc.arrSuffix : ARR_SUFFIX;
        /** @namespace doc.dateFormat */
        DATE_FORMAT = doc.dateFormat ? doc.dateFormat : DATE_FORMAT;

        for (var name in DOC.schemas) {
            var schema = DOC.schemas[name];
            schema.properties = this.properties(schema);
        }

        {
            var tpl = $("#tpl-httpdoc-introduction").html();
            var html = Mustache.render(tpl, DOC);
            $("#httpdoc-head").html(html);
        }

        {
            var tags = [];
            for (var tag in MAP) tags.push(tag);
            var tpl = $("#tpl-httpdoc-modules").html();
            var html = Mustache.render(tpl, tags);
            $("#httpdoc-tags").html(html);
        }

        {
            for (var tag in MAP) {
                this.show(tag);
                break;
            }
        }

        {
            var tpl = $("#tpl-httpdoc-models").html();
            var models = [];
            for (var name in DOC.schemas) {
                var model = DOC.schemas[name];
                model.name = name;

                var type = REF_PREFIX + name + REF_SUFFIX;
                model.value = this.toJSONString(0, type, true).trim();

                models.push(model);
            }
            var html = Mustache.render(tpl, models);
            $("#httpdoc-schemas").html(html);
            $("#httpdoc-schemas").find("[data-toggle='tooltip']").tooltip();

            $("#panel-models").on("show.bs.collapse", function () {
                $(this).parent().find(".glyphicon:first").removeClass("glyphicon-chevron-down").addClass("glyphicon-chevron-up");
            });

            $("#panel-models").on("hide.bs.collapse", function () {
                $(this).parent().find(".glyphicon:first").removeClass("glyphicon-chevron-up").addClass("glyphicon-chevron-down");
            });

            $("#httpdoc-schemas").find(".collapse").on("shown.bs.collapse", function () {
                autosize($(this).find("textarea.autosize"));
            });

            $("#httpdoc-schemas").find(".collapse").on("show.bs.collapse", function (event) {
                event.stopPropagation();
                $(this).parent().find(".glyphicon").removeClass("glyphicon-chevron-down").addClass("glyphicon-chevron-up");
            });

            $("#httpdoc-schemas").find(".collapse").on("hide.bs.collapse", function (event) {
                event.stopPropagation();
                $(this).parent().find(".glyphicon").removeClass("glyphicon-chevron-up").addClass("glyphicon-chevron-down");
            });
        }

        {
            // 读取本地设置
            var setting = localStorage.getItem("setting");
            // 如果本地设置存在则读取并且弹出窗口让用户决定是否要修改
            if (setting) {
                SETTING = JSON.parse(localStorage.getItem("setting"));
                var tpl = $("#tpl-httpdoc-setting").html();
                var html = Mustache.render(tpl, SETTING);
                $("#httpdoc-config").find(".modal-body").html(html);
                $('#httpdoc-config').modal('show');
            }
            // 如果本地设置没有则初始化之
            else {
                SETTING.protocol = DOC.protocol ? DOC.protocol : location.protocol.replace(":", "");
                SETTING.hostname = DOC.hostname ? DOC.hostname : location.hostname;
                SETTING.port = DOC.port ? DOC.port : location.port;
                SETTING.context = DOC.context ? DOC.context : "";
                SETTING.username = "";
                SETTING.password = "";
                SETTING.async = true;
                SETTING.timeout = 0;
                SETTING.withCredentials = false;
                SETTING.queries = [];
                SETTING.headers = [];
                SETTING.cookies = [];
            }
            $('#httpdoc-config').on('show.bs.modal', function () {
                var tpl = $("#tpl-httpdoc-setting").html();
                var html = Mustache.render(tpl, SETTING);
                $("#httpdoc-config").find(".modal-body").html(html);
            });
        }
    };

    this.properties = function (schema) {
        /** @namespace schema.constants */
        if (schema.constants) return {};

        var superclass = schema.superclass;
        var properties = {};
        if (superclass && superclass.startsWith(REF_PREFIX) && superclass.endsWith(REF_SUFFIX)) {
            var name = superclass.substring(REF_PREFIX.length, superclass.length - REF_SUFFIX.length);
            var scm = DOC.schemas[name];
            var props = this.properties(scm);
            for (var key in props) {
                properties[key] = props[key];
            }
        }

        for (var key in schema.properties ? schema.properties : {}) {
            properties[key] = schema.properties[key];
        }

        return properties;
    };

    this.display = function (controllers) {
        for (var i = 0; controllers && i < controllers.length; i++) {
            var controller = controllers[i];
            var operations = controller.operations;
            for (var j = 0; operations && j < operations.length; j++) {
                var operation = operations[j];
                var parameters = operation.parameters;
                for (var k = 0; parameters && k < parameters.length; k++) {
                    var parameter = parameters[k];
                    if (parameter.resolved) continue;
                    else parameter.resolved = true;

                    var type = parameter.type;

                    var value = this.toJSONString(0, type, true).trim();
                    // 如果是字符串则去掉前后双引号
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        value = value.substring(1, value.length - 1);
                    }
                    parameter.value = value;
                }

                var result = operation.result;
                var type = result.type;
                result.value = this.toJSONString(0, type, true).trim();
            }
        }

        {
            var tpl = $("#tpl-httpdoc-apis").html();
            var html = Mustache.render(tpl, controllers);
            $("#httpdoc-controllers").html(html);
            $("#httpdoc-controllers").find("[data-toggle='tooltip']").tooltip();
        }

        $("#httpdoc-controllers").find(".collapse").on("shown.bs.collapse", function () {
            autosize($(this).find("textarea.autosize"));
        });

        $("#httpdoc-controllers").find(".collapse").on("show.bs.collapse", function () {
            $(this).parent().find(".glyphicon").removeClass("glyphicon-chevron-down").addClass("glyphicon-chevron-up");
        });

        $("#httpdoc-controllers").find(".collapse").on("hide.bs.collapse", function () {
            $(this).parent().find(".glyphicon").removeClass("glyphicon-chevron-up").addClass("glyphicon-chevron-down");
        });
    };

    this.toJSONString = function (indent, type, doc) {
        var json = "";

        if (type.startsWith(ARR_PREFIX) && type.endsWith(ARR_SUFFIX)) {
            json += "[\n";
            for (var i = 0; i < indent + 1; i++) json += INDENT;
            json += this.toJSONString(indent + 1, type.substring(ARR_PREFIX.length, type.length - ARR_SUFFIX.length), doc);
            json += "\n";
            for (var i = 0; i < indent; i++) json += INDENT;
            json += "]";
            return json;
        }

        if (type.startsWith(MAP_PREFIX) && type.endsWith(MAP_SUFFIX)) {
            json += "{\n";
            json += "\"\": " + this.toJSONString(indent + 1, type.substring(MAP_PREFIX.length, type.length - MAP_SUFFIX.length), doc);
            json += "\n}";
            return json;
        }

        if (type.startsWith(REF_PREFIX) && type.endsWith(REF_SUFFIX)) {
            var name = type.substring(REF_PREFIX.length, type.length - REF_SUFFIX.length);
            var schema = DOC.schemas[name];

            // 枚举类型
            if (schema.constants) {
                json += "\"";
                for (var con in schema.constants) {
                    json += con;
                    break;
                }
                json += "\"";
                return json;
            }
            // 自定义类型
            else {
                var properties = schema.properties;
                json += "{\n";
                var index = 0;
                for (var key in properties) {
                    if (index++ > 0) json += ",\n";
                    if (doc && properties[key].description) {
                        var descriptions = properties[key].description.split("\n");
                        for (var d = 0; descriptions && d < descriptions.length; d++) {
                            for (var i = 0; i < indent + 1; i++) json += INDENT;
                            json += "// " + descriptions[d].trim() + "\n";
                        }
                    }
                    for (var i = 0; i < indent + 1; i++) json += INDENT;
                    json += "\"" + key + "\": ";
                    json += this.toJSONString(indent + 1, properties[key].type, doc);
                }
                json += "\n";
                for (var i = 0; i < indent; i++) json += INDENT;
                json += "}";
                return json;
            }
        }
        switch (type) {
            case "boolean":
                return "false";
            case "byte":
                return "0";
            case "short":
                return "0";
            case "char":
                return "\" \"";
            case "int":
                return "0";
            case "float":
                return "0.0";
            case "long":
                return "0";
            case "double":
                return "0.0";

            case "Boolean":
                return "false";
            case "Byte":
                return "0";
            case "Short":
                return "0";
            case "Character":
                return "\" \"";
            case "Integer":
                return "0";
            case "Float":
                return "0.0";
            case "Long":
                return "0";
            case "Double":
                return "0.0";

            case "String":
                return "\"string\"";
            case "Number":
                return "0.0";
            case "Date":
                return DATE_FORMAT.toLowerCase() === "timestamp" ? "" + new Date().getTime() : "\"" + new Date().format(DATE_FORMAT) + "\"";
            case "void":
                return "void";
            default:
                return "\"unknown\"";
        }
    };

    this.toJSONObject = function (string) {
        var json = this.clean(string);
        if (json.startsWith("{") && json.endsWith("}")) return JSON.parse(json);
        if (json.startsWith("[") && json.endsWith("]")) return JSON.parse(json);
        if (json.startsWith("\"") && json.endsWith("\"")) return JSON.parse(json);
        if (/^(-?\d+)(\.\d+)?$/.test(json)) return JSON.parse(json);
        return json;
    };

    this.toXMLString = function (indent, tag, type, doc) {
        var xml = "";
        tag = tag ? tag : "xml";
        if (type.startsWith(ARR_PREFIX) && type.endsWith(ARR_SUFFIX)) {
            // 缩进
            for (var i = 0; i < indent; i++) xml += INDENT;
            // 开始
            xml += "<" + tag + ">\n";

            // 内部
            xml += this.toXMLString(indent + 1, tag, type.substring(ARR_PREFIX.length, type.length - ARR_SUFFIX.length), doc);

            // 缩进
            for (var i = 0; i < indent; i++) xml += INDENT;
            // 结束
            xml += "</" + tag + ">\n";
            return xml;
        }

        if (type.startsWith(MAP_PREFIX) && type.endsWith(MAP_SUFFIX)) {
            // 缩进
            for (var i = 0; i < indent; i++) xml += INDENT;
            // 开始
            xml += "<" + tag + ">\n";

            // 内部
            xml += this.toXMLString(indent + 1, tag, type.substring(MAP_PREFIX.length, type.length - MAP_SUFFIX.length), doc);

            // 缩进
            for (var i = 0; i < indent; i++) xml += INDENT;
            // 结束
            xml += "</" + tag + ">\n";
            return xml;
        }

        if (type.startsWith(REF_PREFIX) && type.endsWith(REF_SUFFIX)) {
            var name = type.substring(REF_PREFIX.length, type.length - REF_SUFFIX.length);
            var schema = DOC.schemas[name];

            // 枚举类型
            if (schema.constants) {
                // 缩进
                for (var i = 0; i < indent; i++) xml += INDENT;
                // 开始
                xml += "<" + tag + ">";

                for (var con in schema.constants) {
                    xml += con;
                    break;
                }

                // 结束
                xml += "</" + tag + ">\n";
                return xml;
            }
            // 自定义类型
            else {
                // 缩进
                for (var i = 0; i < indent; i++) xml += INDENT;
                // 开始
                xml += "<" + tag + ">\n";
                var properties = schema.properties;
                for (var key in properties) {
                    if (doc && properties[key].description) {
                        var descriptions = properties[key].description.split("\n");
                        for (var d = 0; descriptions && d < descriptions.length; d++) {
                            for (var i = 0; i < indent + 1; i++) xml += INDENT;
                            xml += "// " + descriptions[d].trim() + "\n";
                        }
                    }
                    xml += this.toXMLString(indent + 1, key, properties[key].type, doc);
                }
                // 缩进
                for (var i = 0; i < indent; i++) xml += INDENT;
                // 结束
                xml += "</" + tag + ">\n";
                return xml;
            }
        }

        // 缩进
        for (var i = 0; i < indent; i++) xml += INDENT;
        // 开始
        xml += "<" + tag + ">";
        switch (type) {
            case "boolean":
                xml += "false";
                break;
            case "byte":
                xml += "0";
                break;
            case "short":
                xml += "0";
                break;
            case "char":
                xml += "<![CDATA[ ]]>";
                break;
            case "int":
                xml += "0";
                break;
            case "float":
                xml += "0.0";
                break;
            case "long":
                xml += "0";
                break;
            case "double":
                xml += "0.0";
                break;

            case "Boolean":
                xml += "false";
                break;
            case "Byte":
                xml += "0";
                break;
            case "Short":
                xml += "0";
                break;
            case "Character":
                xml += "<![CDATA[ ]]>";
                break;
            case "Integer":
                xml += "0";
                break;
            case "Float":
                xml += "0.0";
                break;
            case "Long":
                xml += "0";
                break;
            case "Double":
                xml += "0.0";
                break;

            case "String":
                xml += "<![CDATA[string]]>";
                break;
            case "Number":
                xml += "0.0";
                break;
            case "Date":
                xml += (DATE_FORMAT.toLowerCase() === "timestamp" ? "" + new Date().getTime() : "<![CDATA[" + new Date().format(DATE_FORMAT) + "]]>");
                break;
            case "void":
                xml += "void";
                break;
            default:
                xml += "unknown";
                break;
        }
        // 结束
        xml += "</" + tag + ">\n";
        return xml;
    };

    this.clean = function (string) {
        var lines = string.split("\n");
        var cleaned = "";
        for (var i = 0; lines && i < lines.length; i++) {
            var line = lines[i];
            // 忽略注释行
            if (line.trim().startsWith("//")) continue;
            cleaned += line + '\n';
        }
        cleaned = cleaned.trim();
        return cleaned;
    };

    this.show = function (tag) {
        var controllers = MAP[tag];
        this.display(controllers);
    };

    this.submit = function (btn) {
        var $btn = $(btn);
        var id = $btn.attr("x-operation");
        var method = $btn.attr("x-method");
        var path = $btn.attr("x-path");
        var $operation = $("#operation-" + id);
        var $params = $operation.find(".x-param");

        var paths = [];
        var matrices = [];
        var queries = [];
        var headers = [];
        var cookies = [];
        var bodies = [];

        var self = this;
        // 构建参数
        $params.each(function (index, param) {
            var $param = $(param);
            var name = $param.attr("x-name");
            var scope = $param.attr("x-scope");
            var path = $param.attr("x-path");
            var value = scope === "body" ? self.clean($param.val()) : self.toJSONObject($param.val());
            var metadata = {
                name: name,
                scope: scope,
                path: path,
                value: value
            };
            switch (scope) {
                case "path":
                    paths.push(metadata);
                    break;
                case "matrix":
                    matrices.push(metadata);
                    break;
                case "query":
                    queries.push(metadata);
                    break;
                case "field":
                    queries.push(metadata);
                    break;
                case "header":
                    headers.push(metadata);
                    break;
                case "cookie":
                    cookies.push(metadata);
                    break;
                case "body":
                    bodies.push(metadata);
                    break;
                default:
                    break;
            }
        });

        // 将全局设置放进参数里面但是原则是如果已存在则忽略
        for (var q = 0; SETTING && SETTING.queries && q < SETTING.queries.length; q++) {
            var query = SETTING.queries[q];
            // 先看是否存在
            var exists = false;
            for (var i = 0; queries && i < queries.length; i++) {
                var _query = queries[i];
                if (query.key === _query.name) {
                    exists = true;
                    break;
                }
            }
            if (!exists) queries.push({
                name: query.key,
                scope: "query",
                path: "",
                value: query.value
            });
        }
        for (var h = 0; SETTING && SETTING.headers && h < SETTING.headers.length; h++) {
            var header = SETTING.headers[h];
            // 先看是否存在
            var exists = false;
            for (var i = 0; headers && i < headers.length; i++) {
                var _header = headers[i];
                if (header.key === _header.name) {
                    exists = true;
                    break;
                }
            }
            if (!exists) headers.push({
                name: header.key,
                scope: "header",
                path: "",
                value: header.value
            });
        }
        for (var c = 0; SETTING && SETTING.cookies && c < SETTING.cookies.length; c++) {
            var cookie = SETTING.cookies[c];
            // 先看是否存在
            var exists = false;
            for (var i = 0; cookies && i < cookies.length; i++) {
                var _cookie = cookies[i];
                if (cookie.key === _cookie.name) {
                    exists = true;
                    break;
                }
            }
            if (!exists) cookies.push({
                name: cookie.key,
                scope: "cookie",
                path: "",
                value: cookie.value
            });
        }

        var http = new HTTP();
        http.setting = SETTING;
        http.uri = path;
        http.method = method;
        http.paths = paths;
        http.matrices = matrices;
        http.queries = queries;
        http.headers = headers;
        http.cookies = cookies;
        http.bodies = bodies;
        $btn.button('loading');
        http.xhr.addEventListener("timeout", function () {
            autosize.update(
                $operation.find(".httpdoc-header")
                    .show()
                    .find("textarea")
                    .text("timeout")
            );
        });
        http.execute(function () {
            // 未完成
            if (this.readyState !== 4) return;
            $btn.button('reset');
            var curl = "curl -X " + this.method + " \"" + this.url + "\"";
            for (var key in this.header) {
                var values = this.header[key];
                for (var h = 0; values && h < values.length; h++) curl += " -H \"" + key + ": " + values[h] + "\"";
            }
            if (this.body) {
                var d = this.body.replace(new RegExp("[\r\n]", "g"), "")
                    .replace(new RegExp("\\s+", "g"), " ")
                    .replace(new RegExp("\\\\", "g"), "\\\\")
                    .replace(new RegExp("\"", "g"), "\\\"");
                curl += " -d \"" + d + "\"";
            }

            autosize.update(
                $operation.find(".httpdoc-curl")
                    .show()
                    .find("textarea")
                    .text(curl)
            );

            autosize.update(
                $operation.find(".httpdoc-header")
                    .show()
                    .find("textarea")
                    .text(this.status === 0 ? "" : this.status + " " + (this.statusText ? this.statusText : "") + "\r\n" + this.getAllResponseHeaders())
            );

            var responseText = this.responseText;
            var contentType = this.getResponseHeader("Content-Type");
            if (typeof contentType !== 'string') contentType = "";

            if (contentType.startsWith("application/json")) {
                responseText = JSONConverter.beautify(responseText);
            } else if (contentType.startsWith("application/xml")) {
                responseText = XMLConverter.beautify(responseText);
            }

            autosize.update(
                $operation.find(".httpdoc-result")
                    .show()
                    .find("textarea")
                    .text(responseText)
            );
        });
    };

    this.addSettingRow = function (btn) {
        var tpl = $("#tpl-httpdoc-setting-row").html();
        $(btn).parent().parent().before(tpl);
    };

    this.delSettingRow = function (btn) {
        $(btn).parent().parent().remove();
    };

    this.clearSetting = function () {
        localStorage.removeItem("setting");
        SETTING.protocol = DOC.protocol ? DOC.protocol : location.protocol.replace(":", "");
        SETTING.hostname = DOC.hostname ? DOC.hostname : location.hostname;
        SETTING.port = DOC.port ? DOC.port : location.port;
        SETTING.context = DOC.context ? DOC.context : "";
        SETTING.username = "";
        SETTING.password = "";
        SETTING.async = true;
        SETTING.timeout = 0;
        SETTING.withCredentials = false;
        SETTING.queries = [];
        SETTING.headers = [];
        SETTING.cookies = [];
        var tpl = $("#tpl-httpdoc-setting").html();
        var html = Mustache.render(tpl, SETTING);
        $("#httpdoc-config").find(".modal-body").html(html);
        $('#httpdoc-config').modal('hide');
    };

    this.mergeSetting = function () {
        // 基础设置
        {
            var $basic = $("#httpdoc-setting-basic");
            var protocol = $basic.find("input[name='protocol']").val();
            var hostname = $basic.find("input[name='hostname']").val();
            var port = $basic.find("input[name='port']").val();
            var context = $basic.find("input[name='context']").val();
            SETTING.protocol = protocol && protocol !== "" ? protocol : DOC.protocol ? DOC.protocol : location.protocol.replace(":", "");
            SETTING.hostname = hostname && hostname !== "" ? hostname : DOC.hostname ? DOC.hostname : location.hostname;
            SETTING.port = port && port !== "" && /\d+/.test(port) ? parseInt(port) : DOC.port ? DOC.port : location.port;
            SETTING.context = context && context !== "" ? context : DOC.context ? DOC.context : "";
        }
        // XMLHttpRequest 设置
        {
            var $xhr = $("#httpdoc-setting-xhr");
            var username = $xhr.find("input[name='username']").val();
            var password = $xhr.find("input[name='password']").val();
            var async = $xhr.find("input[name='async']").val();
            var timeout = $xhr.find("input[name='timeout']").val();
            var withCredentials = $xhr.find("input[name='withCredentials']").val();
            SETTING.username = username && username !== "" ? username : "";
            SETTING.password = password && password !== "" ? password : "";
            SETTING.async = async && async !== "" && /(true|false)/g.test(async) ? eval(async) : true;
            SETTING.timeout = timeout && timeout !== "" && /\d+/.test(timeout) ? parseInt(timeout) : 0;
            SETTING.withCredentials = withCredentials && withCredentials !== "" && /(true|false)/g.test(withCredentials) ? eval(withCredentials) : false;
        }
        // Query 设置
        {
            var queries = [];
            var $query = $("#httpdoc-setting-query");
            var $items = $query.find("tr");
            $items.each(function (index, item) {
                var $item = $(item);
                var key = $item.find("input[name='setting-key']").val();
                var value = $item.find("input[name='setting-value']").val();
                if (!key || key === "") return;
                queries.push({
                    key: key,
                    value: value
                });
            });
            SETTING.queries = queries;
        }
        // Header 设置
        {
            var headers = [];
            var $header = $("#httpdoc-setting-header");
            var $items = $header.find("tr");
            $items.each(function (index, item) {
                var $item = $(item);
                var key = $item.find("input[name='setting-key']").val();
                var value = $item.find("input[name='setting-value']").val();
                if (!key || key === "") return;
                headers.push({
                    key: key,
                    value: value
                });
            });
            SETTING.headers = headers;
        }
        // Cookie 设置
        {
            var cookies = [];
            var $cookie = $("#httpdoc-setting-cookie");
            var $items = $cookie.find("tr");
            $items.each(function (index, item) {
                var $item = $(item);
                var key = $item.find("input[name='setting-key']").val();
                var value = $item.find("input[name='setting-value']").val();
                if (!key || key === "") return;
                cookies.push({
                    key: key,
                    value: value
                });
            });
            SETTING.cookies = cookies;
        }
        localStorage.setItem("setting", JSON.stringify(SETTING));
        $('#httpdoc-config').modal('hide');
    };

    this.onConsumeChanged = function (value, id) {
        var $operation = $("#operation-" + id);
        var $textareas = $operation.find("textarea[x-scope='body']");
        var self = this;
        $textareas.each(function (index, textarea) {
            var $textarea = $(textarea);
            var type = $textarea.attr("x-type");
            switch (value) {
                case "application/json":
                    $textarea.text(self.toJSONString(0, type, true).trim());
                    break;
                case "application/xml":
                    var name = type && type.startsWith(REF_PREFIX) && type.endsWith(REF_SUFFIX) ? type.substring(REF_PREFIX.length, type.length - REF_SUFFIX.length) : "xml";
                    $textarea.text(self.toXMLString(0, name, type, true).trim());
                    break;
            }
            autosize.update($textarea);
        });
    };

    this.onProduceChanged = function (value, id) {
        var $operation = $("#operation-" + id);
        var $textarea = $operation.find(".httpdoc-example").find("textarea");
        var type = $textarea.attr("x-type");
        switch (value) {
            case "application/json":
                $textarea.text(this.toJSONString(0, type, true).trim());
                break;
            case "application/xml":
                var name = type && type.startsWith(REF_PREFIX) && type.endsWith(REF_SUFFIX) ? type.substring(REF_PREFIX.length, type.length - REF_SUFFIX.length) : "xml";
                $textarea.text(this.toXMLString(0, name, type, true).trim());
                break;
        }
        autosize.update($textarea);
    };

    return this;
}

/**
 * HTTP Request
 */
function HTTP() {
    this.xhr = new XMLHttpRequest();
    this.setting = {};
    this.uri = "";
    this.method = "";
    this.paths = [];
    this.matrices = [];
    this.queries = [];
    this.headers = [];
    this.cookies = [];
    this.bodies = [];

    /**
     * 执行
     * @param callback 回调函数，this 就是 XMLHttpRequest
     */
    this.execute = function (callback) {
        var xhr = this.xhr;
        xhr.onreadystatechange = callback;

        var method = this.method;
        var url = this.url();
        xhr.method = method;
        xhr.url = url;

        var username = this.setting && this.setting.username ? this.setting.username : null;
        var password = this.setting && this.setting.password ? this.setting.password : null;
        var async = this.setting && this.setting.async ? this.setting.async : true;
        var timeout = this.setting && this.setting.timeout ? this.setting.timeout : 0;
        var withCredentials = this.setting && this.setting.withCredentials ? this.setting.withCredentials : false;

        xhr.open(method, url, async, username, password);
        xhr.timeout = timeout;
        xhr.withCredentials = withCredentials;

        // multipart/form-data
        var bodies = this.bodies;
        if (bodies.length > 1) {
            var multipart = "";
            var CRLF = "\r\n";
            var boundary = this.random(32);
            var header = this.header();
            this.lowercase(header);
            // 从header里面拿出content-type
            var contentType = header["content-type"] ? header['content-type'][0] : null;
            // 如果没有的话 默认用 application/json
            if (!contentType || contentType.trim() === "") {
                contentType = "application/json";
            }
            for (var b = 0; bodies && b < bodies.length; b++) {
                var metadata = bodies[b];
                multipart += "--" + boundary + CRLF;
                multipart += "content-disposition: form-data; name=\"" + encodeURIComponent(metadata.name) + "\"" + CRLF;
                multipart += "content-type: " + contentType + CRLF;
                multipart += CRLF;
                multipart += metadata.value + CRLF;
            }
            multipart += "--" + boundary + "--" + CRLF;
            header['content-type'] = ["multipart/form-data; boundary=" + boundary];
            for (var key in header) {
                var values = header[key];
                for (var h = 0; values && h < values.length; h++) xhr.setRequestHeader(key, values[h]);
            }
            xhr.header = header;
            xhr.body = multipart;
            xhr.send(multipart);
        }
        // 简单请求
        else {
            if (bodies.length > 0) {
                var header = this.header();
                this.lowercase(header);
                // 从header里面拿出content-type
                var contentType = header["content-type"] ? header['content-type'][0] : null;
                // 如果没有的话 默认用 application/json
                if (!contentType || contentType.trim() === "") {
                    header["content-type"] = ["application/json"];
                }
                var body = bodies[0].value;
                for (var key in header) {
                    var values = header[key];
                    for (var h = 0; values && h < values.length; h++) xhr.setRequestHeader(key, values[h]);
                }
                xhr.header = header;
                xhr.body = body;
                xhr.send(body);
            } else {
                var header = this.header();
                this.lowercase(header);
                for (var key in header) {
                    var values = header[key];
                    for (var h = 0; values && h < values.length; h++) xhr.setRequestHeader(key, values[h]);
                }
                xhr.header = header;
                xhr.send();
            }
        }
    };

    /**
     * 获取请求URL
     * @returns {string} URL
     */
    this.url = function () {
        var protocol = this.setting && this.setting.protocol ? this.setting.protocol : location.protocol.replace(":", "");
        var hostname = this.setting && this.setting.hostname ? this.setting.hostname : location.hostname;
        var port = this.setting && this.setting.port ? this.setting.port : location.port;
        var context = this.setting && this.setting.context ? this.setting.context : "";
        var url = protocol + "://" + hostname + (port <= 0 ? "" : ":" + port) + context + this.path();
        var query = this.query();
        if (query && typeof query === 'string' && query !== "") url += url.indexOf("?") < 0 ? "?" + query : "&" + query;
        return url;
    };

    /**
     * 获取请求路径
     * @returns {string} 请求路径
     */
    this.path = function () {
        var path = this.uri;
        for (var i = 0; this.paths && i < this.paths.length; i++) {
            var metadata = this.paths[i];
            var name = metadata.name;
            var value = metadata.value;
            var map = this.flatten(name, value);
            var values = map[name];
            var val = values[0];
            // 追加矩阵参数
            var append = "";
            for (var j = 0; this.matrices && j < this.matrices.length; j++) {
                var matrix = this.matrices[j];
                // 指定路径占位符的或第一个路径占位符
                if (name === matrix.path || (matrix.path === "" && i === 0)) {
                    var m = this.flatten(matrix.name, matrix.value);
                    for (var k in m) {
                        var array = m[k];
                        // 对每个元素都进行URL编码
                        for (var x = 0; array && x < array.length; x++) array[x] = encodeURIComponent(array[x]);
                        if (append !== "") append += ";";
                        append += encodeURIComponent(k);
                        append += "=";
                        append += array.join(",");
                    }
                }
            }
            var replacement = encodeURIComponent(val) + (append === "" ? "" : ";" + append);
            // 替换路径中的占位符
            path = path.replace("{" + name + "}", replacement);
        }
        return path;
    };

    /**
     * 获取查询字符串
     * @returns {string} 查询字符串
     */
    this.query = function () {
        var query = "";
        for (var i = 0; this.queries && i < this.queries.length; i++) {
            var metadata = this.queries[i];
            var map = this.flatten(metadata.name, metadata.value);
            for (var key in map) {
                if (query !== "") query += "&";
                var values = map[key];
                for (var j = 0; values && j < values.length; j++) {
                    query += encodeURIComponent(key);
                    query += "=";
                    query += encodeURIComponent(values[j]);
                }
            }
        }
        return query;
    };

    /**
     * 获取请求头
     */
    this.header = function () {
        var header = {};
        for (var i = 0; this.headers && i < this.headers.length; i++) {
            var metadata = this.headers[i];
            var map = this.flatten(metadata.name, metadata.value);
            for (var key in map) {
                var headerKey = encodeURI(key);
                var headerValues = map[key];
                for (var j = 0; headerValues && j < headerValues.length; j++) headerValues[j] = encodeURI(headerValues[j]);
                header[headerKey] = headerValues;
            }
        }
        var cookie = this.cookie();
        if (cookie && cookie !== "") {
            header['Cookie'] = [cookie];
        }
        return header;
    };

    /**
     * 获取Cookie
     * @returns {string} cookie
     */
    this.cookie = function () {
        var cookie = "";
        for (var i = 0; this.cookies && i < this.cookies.length; i++) {
            var metadata = this.cookies[i];
            var map = this.flatten(metadata.name, metadata.value);
            for (var key in map) {
                var values = map[key];
                for (var j = 0; values && j < values.length; j++) {
                    if (cookie !== "") cookie += "; ";
                    cookie += encodeURI(key);
                    cookie += "=";
                    cookie += encodeURI(values[j]);
                }
            }
        }
        return cookie;
    };

    /**
     * 对象扁平化
     * @param name 名称
     * @param obj 对象
     */
    this.flatten = function (name, obj) {
        name = name ? name.trim() : "";
        var map = {};
        var type = $.isArray(obj) ? "array" : typeof obj;
        switch (type) {
            case "boolean":
                map[name] = ["" + obj];
                break;
            case "number":
                map[name] = ["" + obj];
                break;
            case "string":
                map[name] = ["" + obj];
                break;
            case "array":
                for (var i = 0; obj && i < obj.length; i++) {
                    var am = this.flatten(name + "[" + i + "]", obj[i]);
                    for (var a in am) map[a] = am[a];
                }
                break;
            case "object":
                for (var p in obj) {
                    var n = (name === "" ? p : (name + "." + p));
                    var om = this.flatten(n, obj[p]);
                    for (var o in om) map[o] = om[o];
                }
                break;
            default:
                return map;
        }

        return map;
    };

    /**
     * 获取随机字符串
     * @param length 随机字符串长度，如果参数不合法则返回32位长度的随机字符串
     * @returns {string} 指定长度的随机字符串
     */
    this.random = function (length) {
        var chars = [
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z'
        ];
        var str = "";
        for (var i = 0; i < (length && typeof length === 'number' && length > 0 ? length : 32); i++) {
            var idx = Math.ceil(Math.random() * chars.length);
            str += chars[idx];
        }
        return str;
    };

    /**
     * json 对象key小写化
     * @param obj json 对象
     * @returns {*} json 对象
     */
    this.lowercase = function (obj) {
        for (var key in obj) {
            if (key.toLowerCase() === key) return;
            obj[key.toLowerCase()] = obj[key];
            delete(obj[key]);
        }
        return obj;
    };

    return this;
}

/**
 * 全局可用httpdoc对象
 * @type {HttpDoc} HttpDoc
 */
window.httpdoc = new HttpDoc();