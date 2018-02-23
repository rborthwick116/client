package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2

@SinceVersion(164)
@DependsOn(User::class)
class Buddy : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<User>() }
            .and { it.instanceFields.count() >= 3 }

    class world : OrderMapper.InConstructor.Field(Buddy::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class set : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 2..3 }
    }

    @DependsOn(set::class)
    class int2 : OrderMapper.InMethod.Field(set::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(world::class, int2::class)
    class rank : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it != field<world>() && it != field<int2>() }
    }
}