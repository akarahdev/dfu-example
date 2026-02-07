import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;

public class BarMigrationFixer extends DataFix {
    public BarMigrationFixer(Schema outputSchema, boolean changesType) {
        super(outputSchema, changesType);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return this.writeFixAndRead(
                "Bar = -4",
                this.getInputSchema().getType(TypeReferences.MY_TYPE),
                this.getOutputSchema().getType(TypeReferences.MY_TYPE),
                dyn -> dyn.set("bar", dyn.createInt(-4))
        );
    }
}
