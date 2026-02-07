package foobar;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;

import java.util.Map;
import java.util.function.Supplier;

public class FooDoubledSchema extends Schema {
    public FooDoubledSchema(int versionKey, Schema parent) {
        super(versionKey, parent);
    }

    @Override
    public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
        super.registerTypes(schema, entityTypes, blockEntityTypes);
        schema.registerType(
                true,
                TypeReferences.MY_TYPE,
                () -> DSL.fields(
                        "foo", DSL.constType(DSL.intType()),
                        DSL.remainder()
                )
        );

    }
}
