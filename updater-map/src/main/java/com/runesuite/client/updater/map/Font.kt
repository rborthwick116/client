package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2

@DependsOn(TypeFace::class)
class Font : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<TypeFace>() }
}