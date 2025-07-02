package functionplotter.plotting.utils;

import functionplotter.ast.ASTNodeI;
import functionplotter.utils.COLOR;

public record ColoredNode(ASTNodeI ast, COLOR color) {}