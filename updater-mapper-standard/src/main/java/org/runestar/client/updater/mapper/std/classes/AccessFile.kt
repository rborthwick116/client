package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.id
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.*
import java.io.RandomAccessFile

class AccessFile : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == RandomAccessFile::class.type } == 1 }

    class file : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == RandomAccessFile::class.type }
    }

    class capacity : OrderMapper.InConstructor.Field(AccessFile::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == LONG_TYPE }
    }

    class index : OrderMapper.InConstructor.Field(AccessFile::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == LONG_TYPE }
    }

    @SinceVersion(154)
    @MethodParameters("sync")
    class closeSync : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == RandomAccessFile::close.id } }
    }

    @SinceVersion(154)
    @MethodParameters()
    @DependsOn(closeSync::class)
    class close : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == method<closeSync>().id } }
    }

    @MethodParameters("index")
    class seek : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(LONG_TYPE) }
                .and { it.instructions.any { it.isMethod && it.methodId == RandomAccessFile::seek.id } }
    }

    @MethodParameters()
    class length : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == LONG_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == RandomAccessFile::length.id } }
    }

    @MethodParameters("dst", "dstIndex", "length")
    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.startsWith(ByteArray::class.type, INT_TYPE, INT_TYPE) }
    }

    @MethodParameters("src", "srcIndex", "length")
    class write : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(ByteArray::class.type, INT_TYPE, INT_TYPE) }
    }
}