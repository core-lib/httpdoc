String.prototype.startsWith = function (prefix) {
    return this.length >= prefix.length && this.substring(0, prefix.length) === prefix;
};

String.prototype.endsWith = function (suffix) {
    return this.length >= suffix.length && this.substring(this.length - suffix.length) === suffix;
};

String.prototype.trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, '');
};

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

    this.explore = function () {
        var self = this;
        var httpdocURL = $("#httpdoc-url").val();
        $.ajax({
            url: httpdocURL,
            data: {
                "format.canonical": true
            },
            method: "GET",
            success: function (doc) {
                self.init(doc);
                self.render();
            },
            error: function (xhr) {

            }
        });
    };

    this.init = function (doc) {
        DOC = doc;

        DOC.controllers = DOC.controllers ? DOC.controllers : [];

        // 给对象取一个唯一标识
        var id = 0;
        DOC.controllers.forEach(function (controller) {
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

        REF_PREFIX = doc.refPrefix ? doc.refPrefix : REF_PREFIX;
        REF_SUFFIX = doc.refSuffix ? doc.refSuffix : REF_SUFFIX;
        MAP_PREFIX = doc.mapPrefix ? doc.mapPrefix : MAP_PREFIX;
        MAP_SUFFIX = doc.mapSuffix ? doc.mapSuffix : MAP_SUFFIX;
        ARR_PREFIX = doc.arrPrefix ? doc.arrPrefix : ARR_PREFIX;
        ARR_SUFFIX = doc.arrSuffix ? doc.arrSuffix : ARR_SUFFIX;

        for (var name in DOC.schemas) {
            var schema = DOC.schemas[name];
            schema.properties = this.properties(schema);
        }

        {
            var tpl = $("#httpdoc-schema").html();
            var schemas = [];
            for (var name in DOC.schemas) {
                var schema = DOC.schemas[name];
                schema.name = name;
                schemas.push(schema);
            }
            var html = Mustache.render(tpl, schemas);
            $("#httpdoc-schemas").html(html);
        }

        {
            var tpl = $("#httpdoc-model").html();
            var models = [];
            for (var name in DOC.schemas) {
                var model = DOC.schemas[name];
                model.name = name;

                var type = REF_PREFIX + name + REF_SUFFIX;
                model.value = this.toJSONString(0, type, true);

                models.push(model);
            }
            var html = Mustache.render(tpl, models);
            $("#httpdoc-models").html(html);

            $("#httpdoc-models").find(".collapse").on("shown.bs.collapse", function () {
                autosize($(this).find("textarea.autosize"));
            });

            $("#httpdoc-models").find(".collapse").on("show.bs.collapse", function () {
                $(this).parent().find(".glyphicon").removeClass("glyphicon-chevron-down").addClass("glyphicon-chevron-up");
            });

            $("#httpdoc-models").find(".collapse").on("hide.bs.collapse", function () {
                $(this).parent().find(".glyphicon").removeClass("glyphicon-chevron-up").addClass("glyphicon-chevron-down");
            });
        }

        {
            // 读取本地设置
            var setting = localStorage.getItem("setting");
            // 如果本地设置存在则读取并且弹出窗口让用户决定是否要修改
            if (setting) {
                SETTING = JSON.parse(localStorage.getItem("setting"));
                var tpl = $("#httpdoc-setting").html();
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
                SETTING.queries = [];
                SETTING.headers = [];
                SETTING.cookies = [];
            }
            $('#httpdoc-config').on('show.bs.modal', function () {
                var tpl = $("#httpdoc-setting").html();
                var html = Mustache.render(tpl, SETTING);
                $("#httpdoc-config").find(".modal-body").html(html);
            });
        }
    };

    this.properties = function (schema) {
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

    this.render = function () {
        this.renderNavigation();
        // 第一个tag的内容默认展示
        for (var tag in MAP) {
            var controllers = MAP[tag];
            this.renderControllers(controllers);
            break;
        }
    };

    this.renderNavigation = function () {
        var tags = [];
        for (var tag in MAP) tags.push(tag);
        var tpl = $("#httpdoc-tag").html();
        var html = Mustache.render(tpl, tags);
        $("#httpdoc-tags").html(html);
    };

    this.renderControllers = function (controllers) {
        for (var i = 0; i < controllers.length; i++) {
            var controller = controllers[i];
            var operations = controller.operations;
            for (var j = 0; j < operations.length; j++) {
                var operation = operations[j];
                var parameters = operation.parameters;
                for (var k = 0; k < parameters.length; k++) {
                    var parameter = parameters[k];
                    if (parameter.resolved) continue;
                    else parameter.resolved = true;

                    var type = parameter.type;

                    parameter.value = this.toJSONString(0, type, true);
                }

                var result = operation.result;
                var type = result.type;
                result.value = this.toJSONString(0, type, true);
            }
        }

        {
            var tpl = $("#httpdoc-operation").html();
            var html = Mustache.render(tpl, controllers);
            $("#httpdoc-operations").html(html);
        }

        {
            var tpl = $("#httpdoc-controller").html();
            var html = Mustache.render(tpl, controllers);
            $("#httpdoc-controllers").html(html);
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
            json += this.toJSONString(indent + 1, type.substring(ARR_PREFIX.length, type.length - ARR_SUFFIX.length));
            json += "\n";
            for (var i = 0; i < indent; i++) json += INDENT;
            json += "]";
            return json;
        }

        if (type.startsWith(MAP_PREFIX) && type.endsWith(MAP_SUFFIX)) {
            json += "{\n";
            json += "\"\": " + this.toJSONString(indent + 1, type.substring(MAP_PREFIX.length, type.length - MAP_SUFFIX.length));
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
                        for (var i = 0; i < indent + 1; i++) json += INDENT;
                        json += "// " + properties[key].description + "\n";
                    }
                    for (var i = 0; i < indent + 1; i++) json += INDENT;
                    json += "\"" + key + "\": ";
                    json += this.toJSONString(indent + 1, properties[key].type);
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
                return "\"" + new Date().format('yyyy-MM-dd HH:mm:ss') + "\"";
            default:
                return "\"unknown\"";
        }
    };

    this.toJSONObject = function (string) {
        var lines = string.split("\n");
        var json = "";
        for (var i = 0; i < lines.length; i++) {
            var line = lines[i];
            // 忽略注释行
            if (line.trim().startsWith("//")) continue;
            json += line + '\n';
        }
        return JSON.parse(json);
    };

    this.show = function (tag) {
        var controllers = MAP[tag];
        this.renderControllers(controllers);
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
            var value = self.toJSONObject($param.val());
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

        // 路径占位符替换
        for (var i = 0; i < paths.length; i++) {
            var metadata = paths[i];
            var name = metadata.name;
            var value = metadata.value;
            var map = this.flatten(name, value);
            var values = map[name];
            var val = values[0];
            // 追加矩阵参数
            var append = "";
            for (var j = 0; j < matrices.length; j++) {
                var matrix = matrices[j];
                // 指定路径占位符的或第一个路径占位符
                if (name === matrix.path || (matrix.path === "" && i === 0)) {
                    var m = this.flatten(matrix.name, matrix.value);
                    for (var k in m) {
                        var vals = m[k];
                        // 对每个元素都进行URL编码
                        for (var x = 0; x < vals.length; x++) vals[x] = encodeURIComponent(vals[x]);
                        if (append !== "") append += ";";
                        append += encodeURIComponent(k);
                        append += "=";
                        append += vals.join(",");
                    }
                }
            }
            var replacement = encodeURIComponent(val) + (append === "" ? "" : ";" + append);
            // 替换路径中的占位符
            path = path.replace("{" + name + "}", replacement);
        }

        // 查询参数拼接
        var query = "";
        for (var i = 0; i < queries.length; i++) {
            var metadata = queries[i];
            var map = this.flatten(metadata.name, metadata.value);
            for (var key in map) {
                if (query !== "") query += "&";
                var values = map[key];
                for (var j = 0; j < values.length; j++) {
                    query += encodeURIComponent(key);
                    query += "=";
                    query += encodeURIComponent(values[j]);
                }
            }
        }
        if (query !== "") path += path.indexOf("?") < 0 ? ("?" + query) : ("&" + query);

        // 处理请求头
        var header = {};
        for (var i = 0; i < headers.length; i++) {
            var metadata = headers[i];
            var map = this.flatten(metadata.name, metadata.value);
            for (var key in map) {
                var headerKey = encodeURIComponent(key);
                var headerValues = map[key];
                for (var j = 0; j < map[key].length; j++) headerValues[j] = encodeURIComponent(headerValues[j]);
                header[headerKey] = headerValues;
            }
        }

        // 处理请Cookie
        var cookie = "";
        for (var i = 0; i < cookies.length; i++) {
            var metadata = cookies[i];
            var map = this.flatten(metadata.name, metadata.value);
            for (var key in map) {
                var values = map[key];
                for (var j = 0; j < values.length; j++) {
                    if (cookie !== "") query += ";";
                    cookie += encodeURIComponent(key);
                    cookie += "=";
                    cookie += encodeURIComponent(values[j]);
                }
            }
        }
        if (cookie !== "") header["Cookie"] = [cookie];

        // 处理请求体
        var body = bodies.length > 0 ? bodies[0].value : {};

        $.ajax({
            url: path,
            method: method,
            headers: header,
            data: body,
            success: function (result) {
                alert(JSON.stringify(result));
            },
            error: function (xhr) {
                alert(JSON.stringify(xhr));
            }
        });

    };

    this.addSettingRow = function (btn) {
        var row = $("#httpdoc-setting-row").html();
        $(btn).parent().parent().before(row);
    };

    this.delSettingRow = function (btn) {
        $(btn).parent().parent().remove();
    };

    this.clearSetting = function (btn) {
        localStorage.removeItem("setting");
        SETTING.protocol = DOC.protocol ? DOC.protocol : location.protocol.replace(":", "");
        SETTING.hostname = DOC.hostname ? DOC.hostname : location.hostname;
        SETTING.port = DOC.port ? DOC.port : location.port;
        SETTING.context = DOC.context ? DOC.context : "";
        SETTING.queries = [];
        SETTING.headers = [];
        SETTING.cookies = [];
        var tpl = $("#httpdoc-setting").html();
        var html = Mustache.render(tpl, SETTING);
        $("#httpdoc-config").find(".modal-body").html(html);
        $('#httpdoc-config').modal('hide');
    };

    this.mergeSetting = function (btn) {
        // 基础设置
        {
            var $basic = $("#httpdoc-setting-basic");
            var protocol = $basic.find("input[name='protocol']").val();
            var hostname = $basic.find("input[name='hostname']").val();
            var port = $basic.find("input[name='port']").val();
            var context = $basic.find("input[name='context']").val();
            SETTING.protocol = protocol && protocol !== "" ? protocol : location.protocol.replace(":", "");
            SETTING.hostname = hostname && hostname !== "" ? hostname : location.hostname;
            SETTING.port = port && port !== "" && /\d+/.test(port) ? parseInt(port) : location.port;
            SETTING.context = context && context !== "" ? context : "";
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

    /**
     * 对象扁平化
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
                for (var i = 0; i < obj.length; i++) {
                    var m = this.flatten(name + "[" + i + "]", obj[i]);
                    for (var k in m) map[k] = m[k];
                }
                break;
            case "object":
                for (var p in obj) {
                    var n = (name === "" ? p : (name + "." + p));
                    var m = this.flatten(n, obj[p]);
                    for (var k in m) map[k] = m[k];
                }
                break;
            default:
                return map;
        }

        return map;
    };

}

window.httpdoc = new HttpDoc();