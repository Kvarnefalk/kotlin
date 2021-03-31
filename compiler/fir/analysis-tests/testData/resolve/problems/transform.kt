sealed class CompositeTransformResult<out T : Any> {

    class Single<out T : Any>(val _single: T) : CompositeTransformResult<T>()

    class Multiple<out T : Any>(val _list: List<T>) : CompositeTransformResult<T>()

    companion object {
        fun <T : Any> single(t: T) = Single(t)
        fun <T : Any> many(l: List<T>) = Multiple(l)
    }

    val single: T
        get() = (this as Single<*>)._single as T
}

interface FirElement {
    fun <E : FirElement, D> transform(visitor: FirTransformer<D>, data: D): CompositeTransformResult<E>
}

abstract class FirVisitor<out R, in D>

abstract class FirTransformer<in D> : FirVisitor<CompositeTransformResult<FirElement>, D>()

interface FirAnnotationContainer : FirElement

interface FirStatement : FirAnnotationContainer

interface FirTypeParameterRefsOwner : FirElement

interface FirDeclaration : FirElement

interface FirAnnotatedDeclaration : FirDeclaration, FirAnnotationContainer

interface FirSymbolOwner<E> : FirElement where E : FirSymbolOwner<E>, E : FirDeclaration

interface FirClassLikeDeclaration<F : FirClassLikeDeclaration<F>> : FirAnnotatedDeclaration, FirStatement, FirSymbolOwner<F>

interface FirClass<F : FirClass<F>> : FirClassLikeDeclaration<F>, FirStatement, FirTypeParameterRefsOwner

private class FirApplySupertypesTransformer() : FirTransformer<Nothing?>()

fun <F : FirClass<F>> F.runSupertypeResolvePhaseForLocalClass(): F {
    val applySupertypesTransformer = FirApplySupertypesTransformer()
    return this.<!UNRESOLVED_REFERENCE!>transform<!><F, Nothing?>(applySupertypesTransformer, null).single
}
