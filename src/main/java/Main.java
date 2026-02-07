import com.google.gson.JsonObject;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;

void main() {
    var dataFixerBuilder = new DataFixerBuilder(2);
    var v0 = dataFixerBuilder.addSchema(0, RootSchema::new);
    var v1 = dataFixerBuilder.addSchema(1, FooDoubledSchema::new);
    dataFixerBuilder.addFixer(new FooDoublingFixer(v1, true));
    var v2 = dataFixerBuilder.addSchema(2, BarFieldMigrationSchema::new);
    dataFixerBuilder.addFixer(new BarMigrationFixer(v2, true));

    var dataFixer = dataFixerBuilder.build().fixer();

    System.out.println(dataFixer);

    var myData = new JsonObject();
    myData.addProperty("foo", 3);

    var dynamicData = new Dynamic<>(JsonOps.INSTANCE, myData);
    System.out.println(dynamicData);
    var updatedDynamicData = dataFixer.update(
            TypeReferences.MY_TYPE,
            dynamicData,
            0,
            2
    );
    System.out.println(updatedDynamicData);
}
