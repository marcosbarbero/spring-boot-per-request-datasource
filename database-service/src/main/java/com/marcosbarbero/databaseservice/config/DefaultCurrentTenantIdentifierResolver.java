package com.marcosbarbero.databaseservice.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class DefaultCurrentTenantIdentifierResolver implements CurrentTenantIdentifierResolver {
    @Override
    public String resolveCurrentTenantIdentifier() {
        return "DEFAULT";
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
