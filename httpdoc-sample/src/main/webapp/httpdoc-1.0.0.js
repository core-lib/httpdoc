function HttpDoc() {
    var DOC = {};
    var MAP = {};
    var REF_PREFIX = "$/schemas/";
    var REF_SUFFIX = "";
    var MAP_PREFIX = "Dictionary<String,";
    var MAP_SUFFIX = ">";
    var ARR_PREFIX = "";
    var ARR_SUFFIX = "[]";

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
        DOC.controllers.forEach(function (controller) {
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

        String.prototype.startsWith = function (prefix) {
            return this.length >= prefix.length && this.substring(0, prefix.length) === prefix;
        };

        String.prototype.endsWith = function (suffix) {
            return this.length >= suffix.length && this.substring(this.length - suffix.length) === suffix;
        }
    };

    this.properties = function (schema) {
        if (schema.constants) return null;

        var superclass = schema.superclass;
        var properties = schema.properties ? schema.properties : {};
        if (superclass && superclass.startsWith(REF_PREFIX) && superclass.endsWith(REF_SUFFIX)) {
            var name = superclass.substring(REF_PREFIX.length, superclass.length - REF_SUFFIX.length);
            var scm = DOC.schemas[name];
            var props = this.properties(scm);
            for (var key in props) properties[key] = props[key];
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
        $("#httpdoc-navigation").html(html);
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
                    parameter.value = this.toJSONString(0, type);
                }
            }
        }

        var tpl = $("#httpdoc-controller").html();
        var html = Mustache.render(tpl, controllers);
        $("#httpdoc-content").html(html);
    };

    this.toJSONString = function (indent, type) {
        var json = "";

        if (type.startsWith(ARR_PREFIX) && type.endsWith(ARR_SUFFIX)) {
            json += "[\n";
            json += this.toJSONString(indent + 1, type.substring(ARR_PREFIX.length, type.length - ARR_SUFFIX.length));
            json += "\n]";
            return json;
        }

        if (type.startsWith(MAP_PREFIX) && type.endsWith(MAP_SUFFIX)) {
            json += "{\n";
            json += "    \"\": " + this.toJSONString(indent + 1, type.substring(MAP_PREFIX.length, type.length - MAP_SUFFIX.length));
            json += "\n}";
            return json;
        }

        if (type.startsWith(REF_PREFIX) && type.endsWith(REF_SUFFIX)) {
            var name = type.substring(REF_PREFIX.length, type.length - REF_SUFFIX.length);
            var schema = DOC.schemas[name];

            // 枚举类型
            if (schema.constants) {
                json += "(";
                for (var i = 0; i < schema.constants.length; i++) {
                    if (i === 0) json += "|";
                    json += schema.constants[i];
                }
                json += ")";
                return json;
            }
            // 自定义类型
            else {
                var properties = schema.properties;
                json += "{\n";
                var index = 0;
                for (var key in properties) {
                    if (index++ > 0) json += ",\n";
                    json += "    \"" + key + "\": ";
                    json += this.toJSONString(indent + 1, properties[key].type);
                }
                json += "\n}";
                return json;
            }

            return json;
        }

        return "\"" + type + "\"";
    };

    this.show = function (tag) {
        var controllers = MAP[tag];
        this.renderControllers(controllers);
    };

}

window.httpdoc = new HttpDoc();