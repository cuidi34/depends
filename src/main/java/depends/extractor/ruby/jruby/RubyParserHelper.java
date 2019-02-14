package depends.extractor.ruby.jruby;

import java.util.ArrayList;
import java.util.List;

import org.jrubyparser.ast.AssignableNode;
import org.jrubyparser.ast.CallNode;
import org.jrubyparser.ast.ClassVarAsgnNode;
import org.jrubyparser.ast.ClassVarDeclNode;
import org.jrubyparser.ast.Colon2ConstNode;
import org.jrubyparser.ast.Colon2Node;
import org.jrubyparser.ast.Colon3Node;
import org.jrubyparser.ast.ConstDeclNode;
import org.jrubyparser.ast.ConstNode;
import org.jrubyparser.ast.DAsgnNode;
import org.jrubyparser.ast.DefsNode;
import org.jrubyparser.ast.FCallNode;
import org.jrubyparser.ast.GlobalAsgnNode;
import org.jrubyparser.ast.INameNode;
import org.jrubyparser.ast.InstAsgnNode;
import org.jrubyparser.ast.ListNode;
import org.jrubyparser.ast.LocalAsgnNode;
import org.jrubyparser.ast.MultipleAsgnNode;
import org.jrubyparser.ast.Node;
import org.jrubyparser.ast.UnaryCallNode;
import org.jrubyparser.ast.VCallNode;

import depends.entity.ContainerEntity;
import depends.extractor.ruby.RubyBuiltInType;
import depends.extractor.ruby.RubyHandlerContext;

public class RubyParserHelper {
	private static RubyParserHelper inst = new RubyParserHelper();
	public static RubyParserHelper getInst() {
		return inst;
	}

	private RubyBuiltInType buildInType;
	
	private RubyParserHelper() {
		this.buildInType = new RubyBuiltInType();
	}
	
	
	public String getName(Node node) {
		String name = "";
		if (node instanceof INameNode) {
			name = ((INameNode)node).getName();
			if (node instanceof Colon2Node) {
				Node left = ((Colon2Node)node).getLeftNode();
				if (left!=null) {
					name = getName(left) + "."+name;
				}
			}
		}
		System.out.println(name);
		return name.length()>0?name:null;
	}

	public boolean isFunctionCall(Node ctx) {
		return ctx instanceof CallNode ||
				ctx instanceof FCallNode ||
				ctx instanceof UnaryCallNode ||
				ctx instanceof VCallNode;
	}
	
	public List<String> getName(AssignableNode ctx) {
		List<String> names = new ArrayList<>();
		if (ctx instanceof ClassVarAsgnNode) {
			names.add(((ClassVarAsgnNode)ctx).getName());
		}else if (ctx instanceof ClassVarDeclNode) {
			names.add(((ClassVarDeclNode)ctx).getName());
		}else if (ctx instanceof ConstDeclNode) {
			names.add(((ConstDeclNode)ctx).getName());
		}else if (ctx instanceof DAsgnNode) {
			names.add(((DAsgnNode)ctx).getName());
		}else if (ctx instanceof GlobalAsgnNode) {
			names.add(((GlobalAsgnNode)ctx).getName());
		}else if (ctx instanceof InstAsgnNode) {
			names.add(((InstAsgnNode)ctx).getName());
		}else if (ctx instanceof LocalAsgnNode) {
			names.add(((LocalAsgnNode)ctx).getName());
		}else if (ctx instanceof MultipleAsgnNode) {
			ListNode pre = ((MultipleAsgnNode)ctx).getPre();
			Node rest = ((MultipleAsgnNode)ctx).getRest();
			ListNode post = ((MultipleAsgnNode)ctx).getPost();
			if (pre!=null) {
				for ( Node preNode:pre.childNodes()) {
					
				}
			}
		}
		return names;
	}

	public String getReciever(Node ctx) {
		Node receiver = null;
		if (ctx instanceof CallNode) {
			receiver = ((CallNode)ctx).getReceiver();
		}else if (ctx instanceof DefsNode) {
			receiver = ((DefsNode)ctx).getReceiver();
		}
		if (receiver==null) {
			return null;
		}
		if (receiver instanceof INameNode) {
			return ((INameNode)receiver).getName();
		}
		return null;
	}

	public ContainerEntity getScopeOfVar(AssignableNode node, RubyHandlerContext context) {
		if (node instanceof LocalAsgnNode) return context.lastContainer();
		if (node instanceof InstAsgnNode) return context.currentType();
		if (node instanceof ClassVarAsgnNode) return context.currentType();
		if (node instanceof GlobalAsgnNode) return context.globalScope();
		if (node instanceof DAsgnNode) return context.lastContainer();
		return context.lastContainer();
	}

	public boolean isArithMeticOperator(String name) {
		return name.equals("+") ||
				name.equals("-") ||
				name.equals("*") ||
				name.equals("/") ||
				name.equals("**") ||
				name.equals("%") ||
				name.equals("&") ||
				name.equals("<") ||
				name.equals("<=") ||
				name.equals(">") ||
				name.equals(">=") ||
				name.equals("==") ||
				name.equals("!=") ||
				name.equals("===") ||
				name.equals("<<") ||
				name.equals(">>") ||
				name.equals("~") ||
				name.equals("!") ||
				name.equals("^");
	}
	
	public boolean isCommonOperator(String name) {
		return this.buildInType.isBuildInMethod(name);
	}
}
