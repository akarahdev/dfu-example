package foobar;

import com.mojang.datafixers.*;
import com.mojang.datafixers.schemas.Schema;

public class FooDoublingFixer extends DataFix {
    public FooDoublingFixer(Schema outputSchema, boolean changesType) {
        super(outputSchema, changesType);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped(
                "Multiply foo by 2",
                this.getInputSchema().getType(TypeReferences.MY_TYPE),
                this.getOutputSchema().getType(TypeReferences.MY_TYPE),
                typed -> {
                    System.out.println(
                            "remainder: " +
                            typed.get(DSL.remainderFinder())
                    );
                    return typed.update(
                            DSL.fieldFinder("foo", DSL.intType()),
                            v -> v * 2
                    );
                }
        );
    }
}
