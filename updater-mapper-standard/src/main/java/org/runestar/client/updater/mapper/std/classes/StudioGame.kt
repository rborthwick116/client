package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Opcodes.LDC
import org.objectweb.asm.Type

class StudioGame : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.interfaces.isNotEmpty() }
            .and { it.classInitializer != null }
            .and { it.classInitializer!!.instructions.any { it.opcode == LDC && it.ldcCst == "runescape" } }
            .and { it.classInitializer!!.instructions.any { it.opcode == LDC && it.ldcCst == "RuneScape" } }

    @DependsOn(Enumerated.ordinal::class)
    class ordinal : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<Enumerated.ordinal>().mark }
    }

    class id : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Type.INT_TYPE }
    }

    class name : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == String::class.type }
    }
}