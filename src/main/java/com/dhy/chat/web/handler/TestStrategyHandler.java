package com.dhy.chat.web.handler;

/**
 * @author vghosthunter
 */
public class TestStrategyHandler<T, R> extends AbstractStrategyRouter<T, R> implements StrategyHandler {

    @Override
    protected StrategyMapper<T, R> registerStrategyMapper() {
        return new StrategyMapper<T, R>() {
            @Override
            public StrategyHandler<T, R> get(T param) {
                return null;
            }
        };
    }

    @Override
    public Object apply(Object param) {
        return null;
    }
}
