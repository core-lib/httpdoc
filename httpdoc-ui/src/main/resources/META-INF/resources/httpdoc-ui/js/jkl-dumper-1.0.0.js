// ================================================================
//  jkl-dumper.js ---- JavaScript Object Dumper
//  Copyright 2005-2006 Kawasaki Yusuke <u-suke@kawa.net>
//  2005/05/18 - First Release
//  2006/09/04 - http://www.rfc-editor.org/rfc/rfc4627.txt
//  ===============================================================

/******************************************************************

 <script type="text/javascript" src="./jkl-dumper.js" charset="Shift_JIS"></script>
 <script><!--
 var data = {
            string: "string",
            array:  [ 1, 2, 3 ],
            hash:   { key1: "value1", key2: "value2" },
            data1:  null,
            data2:  true,
            data3:  false
        };
 var dumper = new JKL.Dumper();
 document.write( dumper.dump( data ) );
 //--></script>

 ******************************************************************/

if (typeof(JKL) == 'undefined') JKL = function () {
};

//  JKL.Dumper Constructor

JKL.Dumper = function () {
    return this;
};

//  Dump the data into JSON format

JKL.Dumper.prototype.dump = function (data, offset) {
    if (typeof(offset) == "undefined") offset = "";
    var nextoff = offset + "  ";
    switch (typeof(data)) {
        case "string":
            return '"' + this.escapeString(data) + '"';
            break;
        case "number":
            return data;
            break;
        case "boolean":
            return data ? "true" : "false";
            break;
        case "undefined":
            return "null";
            break;
        case "object":
            if (data == null) {
                return "null";
            } else if (data.constructor == Array) {
                var array = [];
                for (var i = 0; i < data.length; i++) {
                    array[i] = this.dump(data[i], nextoff);
                }
                return "[\n" + nextoff + array.join(",\n" + nextoff) + "\n" + offset + "]";
            } else {
                var array = [];
                for (var key in data) {
                    var val = this.dump(data[key], nextoff);
//              if ( key.match( /[^A-Za-z0-9_]/ )) {
                    key = '"' + this.escapeString(key) + '"';
//              }
                    array[array.length] = key + ": " + val;
                }
                if (array.length == 1 && !array[0].match(/[\n\{\[]/)) {
                    return "{ " + array[0] + " }";
                }
                return "{\n" + nextoff + array.join(",\n" + nextoff) + "\n" + offset + "}";
            }
            break;
        default:
            return data;
            // unsupported data type
            break;
    }
};

//  escape '\' and '"'

JKL.Dumper.prototype.escapeString = function (str) {
    return str.replace(/\\/g, "\\\\").replace(/\"/g, "\\\"");
};

//  ===============================================================
var formatXml = function (xml) {
    var reg = /(>)(<)(\/*)/g;
    var wsexp = / *(.*) +\n/g;
    var contexp = /(<.+>)(.+\n)/g;
    xml = xml.replace(reg, '$1\n$2$3').replace(wsexp, '$1\n').replace(contexp, '$1\n$2');
    var pad = 0;
    var formatted = '';
    var lines = xml.split('\n');
    var indent = 0;
    var lastType = 'other';
    // 4 types of tags - single, closing, opening, other (text, doctype, comment) - 4*4 = 16 transitions
    var transitions = {
        'single->single': 0,
        'single->closing': -1,
        'single->opening': 0,
        'single->other': 0,
        'closing->single': 0,
        'closing->closing': -1,
        'closing->opening': 0,
        'closing->other': 0,
        'opening->single': 1,
        'opening->closing': 0,
        'opening->opening': 1,
        'opening->other': 1,
        'other->single': 0,
        'other->closing': -1,
        'other->opening': 0,
        'other->other': 0
    };

    for (var i = 0; i < lines.length; i++) {
        var ln = lines[i];
        var single = Boolean(ln.match(/<.+\/>/)); // is this line a single tag? ex. <br />
        var closing = Boolean(ln.match(/<\/.+>/)); // is this a closing tag? ex. </a>
        var opening = Boolean(ln.match(/<[^!].*>/)); // is this even a tag (that's not <!something>)
        var type = single ? 'single' : closing ? 'closing' : opening ? 'opening' : 'other';
        var fromTo = lastType + '->' + type;
        lastType = type;
        var padding = '';

        indent += transitions[fromTo];
        for (var j = 0; j < indent; j++) {
            padding += '    ';
        }
        if (fromTo == 'opening->closing')
            formatted = formatted.substr(0, formatted.length - 1) + ln + '\n'; // substr removes line break (\n) from prev loop
        else
            formatted += padding + ln + '\n';
    }

    return formatted;
};