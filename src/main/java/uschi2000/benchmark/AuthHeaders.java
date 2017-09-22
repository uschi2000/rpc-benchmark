package uschi2000.benchmark;

import io.grpc.Context;
import io.grpc.Metadata;

public final class AuthHeaders {

    public static Metadata.Key<String> AUTH_HEADER = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
    public static Context.Key<String> BEARER_TOKEN = Context.key("Authorization");

    private AuthHeaders() {}

}
