function HttpDoc() {
    var DOC = {};
    var MAP = {};

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
                DOC = doc;
                self.render();
            },
            error: function (xhr) {

            }
        });
    };

    this.render = function () {
        /** @namespace DOC.controllers */
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
        var tpl = $("#httpdoc-controller").html();
        var html = Mustache.render(tpl, controllers);
        $("#httpdoc-content").html(html);
    };

    this.show = function (tag) {
        var controllers = MAP[tag];
        this.renderControllers(controllers);
    }
}

window.httpdoc = new HttpDoc();