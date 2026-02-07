import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;

import java.util.Map;
import java.util.function.Supplier;

public class BarFieldMigrationSchema extends Schema {
    public BarFieldMigrationSchema(int versionKey, Schema parent) {
        super(versionKey, parent);
    }

    @Override
    public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
        super.registerTypes(schema, entityTypes, blockEntityTypes);
        schema.registerType(
                true,
                TypeReferences.MY_TYPE,
                () -> {
                    System.out.println(schema.getParent());
                    return DSL.fields(
                            "bar", DSL.constType(DSL.intType()),
                            "foo", DSL.constType(DSL.intType())
                    );
                }

        );

    }
}
