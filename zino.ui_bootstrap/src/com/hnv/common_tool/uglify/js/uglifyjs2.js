var options = {
        ast: false,
        mangle: true,
        mangle_toplevel: false,
        squeeze: true,
        make_seqs: true,
        dead_code: true,
        verbose: false,
        show_copyright: true,
        out_same_file: false,
        max_line_length: 32 * 1024,
        unsafe: false,
        reserved_names: null,
        codegen_options: {
                ascii_only: false,
                beautify: false,
                indent_level: 4,
                indent_start: 0,
                quote_keys: false,
                space_colon: false
        },
        output: true            // stdout
};

var args = jsp.slice(uglify_args, 0);

//println("args: "+args);

var filename;

//out: while (args.length > 0) {
while (args.length > 0) {
        var v = ""+args.shift(); //convert object to String
        switch (v) {
            case "-b":
            case "--beautify":
                options.codegen_options.beautify = true;
                break;
            case "-i":
            case "--indent":
                options.codegen_options.indent_level = args.shift();
                break;
            case "-q":
            case "--quote-keys":
                options.codegen_options.quote_keys = true;
                break;
            case "-mt":
            case "--mangle-toplevel":
                options.mangle_toplevel = true;
                break;
            case "--no-mangle":
            case "-nm":
                options.mangle = false;
                break;
            case "--no-squeeze":
            case "-ns":
                options.squeeze = false;
                break;
            case "--no-seqs":
                options.make_seqs = false;
                break;
            case "--no-dead-code":
                options.dead_code = false;
                break;
            case "--no-copyright":
            case "-nc":
                options.show_copyright = false;
                break;
            case "-o":
            case "--output":
                options.output = args.shift();
                break;
            case "--overwrite":
                options.out_same_file = true;
                break;
            case "-v":
            case "--verbose":
                options.verbose = true;
                break;
            case "--ast":
                options.ast = true;
                break;
            case "--unsafe":
                options.unsafe = true;
                break;
            case "--max-line-len":
                options.max_line_length = parseInt(args.shift(), 10);
                break;
            case "--reserved-names":
                options.reserved_names = args.shift().split(",");
                break;
            case "--ascii":
                options.codegen_options.ascii_only = true;
                break;
            default:
                filename = v;
                break out;
        }
}

if (options.verbose) {
        pro.set_logger(function(msg){
                sys.debug(msg);
        });
}

jsp.set_logger(function(msg){
        sys.debug(msg);
});


function output(text) {
		
        var out;
        if (options.out_same_file && filename)
                options.output = filename;
				
		if (options.output === true) {		//add a default output rules: filename.min.js
				var parts = filename.split(".");
				parts.pop();
				parts.push("min");
				parts.push("js");
				var output = parts.join(".");
				out = new java.io.FileWriter(output);
				
				sys.debug("Output file: "+ output );
		}
        else if (options.output === "stdout") {
                out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(java.lang.System.out));
        } else {
                out = new java.io.FileWriter(options.output);
				
				sys.debug("Output file: "+ options.output );
        }
		
        out.write(text);
		out.flush();
		
		
        if (options.output !== true) {
                out.close();
        }
};


// --------- main ends here.


function show_copyright(comments) {
        var ret = "";
        for (var i = 0; i < comments.length; ++i) {
                var c = comments[i];
                if (c.type == "comment1") {
                        ret += "//" + c.value + "\n";
                } else {
                        ret += "/*" + c.value + "*/";
                }
        }
        return ret;
};


function squeeze_it(code) {

        var result = "";
        if (options.show_copyright) {
                var tok = jsp.tokenizer(code), c;
                c = tok();
                result += show_copyright(c.comments_before);
        }
		
       // try {
                var ast = time_it("parse", function(){ return jsp.parse(code); });
                if (options.mangle) ast = time_it("mangle", function(){
                        return pro.ast_mangle(ast, {
                                toplevel: options.mangle_toplevel,
                                except: options.reserved_names
                        });
                });
                if (options.squeeze) ast = time_it("squeeze", function(){

                        ast = pro.ast_squeeze(ast, {
                                make_seqs  : options.make_seqs,
                                dead_code  : options.dead_code,
                                keep_comps : !options.unsafe
                        });

                        if (options.unsafe)
                                ast = pro.ast_squeeze_more(ast);
								
                        return ast;
                });
                if (options.ast)
                        return sys.inspect(ast, null, null);
                result += time_it("generate", function(){ return pro.gen_code(ast, options.codegen_options) });
                if (!options.codegen_options.beautify && options.max_line_length) {
                        result = time_it("split", function(){ return pro.split_lines(result, options.max_line_length) });
                }
				
				
                return result;
       // } catch(ex) {
                //sys.debug(ex.stack);
        //        sys.debug(sys.inspect(ex));
       // }
};

function time_it(name, cont) {
        if (!options.verbose)
                return cont();
        var t1 = new Date().getTime();
        try { return cont(); }
        finally { sys.debug("// " + name + ": " + ((new Date().getTime() - t1) / 1000).toFixed(3) + " sec."); }
};


function uglify(){

	if (filename) {
	
		var input=new java.io.BufferedReader(new java.io.FileReader(filename));
		var text="",line="";
		while((line = input.readLine())!= null)
			text += line+'\n';
		input.close();
		
		var result = squeeze_it(text);
		
		if(uglify_no_output == false ){
			output(result);
		}	
		return result;
	}
}
