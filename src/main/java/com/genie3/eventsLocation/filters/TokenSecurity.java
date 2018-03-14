    package com.genie3.eventsLocation.filters;

    import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;


    public class TokenSecurity {

        private static RsaJsonWebKey rsaJsonWebKey = null;
        private static String issuer = "events.com";
        private static int timeToExpire = 129600;

        final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(  AuthentificationFilter.class );

        // 	Generate an RSA key pair,
        // which will be used for signing and verification of the JWT, wrapped in a JWK
        static {
            try {

                ClassLoader classLoader = ClassLoader.getSystemClassLoader();

               // System.out.println("11 - " + classLoader.getResource("log4j2.xml").getFile();

                File file1 = new File("key/events_map");
                File file2 = new File("key/events_map.pub");



                    Path p1 =  Paths.get(file1.toURI());
                    Path p2 =  Paths.get(file2.toURI());
                    String privateKeyContent = new String(Files.readAllBytes(p1));
                    String publicKeyContent = new String(Files.readAllBytes(p2));

                    privateKeyContent = privateKeyContent.replaceAll("\\r|\\n","")
                            .replace("-----BEGIN PRIVATE KEY-----","")
                            .replace("-----END PRIVATE KEY-----","");
                    
                   /* Generate private key*/



                    PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
                    KeyFactory kf = KeyFactory.getInstance("RSA");
                    PrivateKey pvt = kf.generatePrivate(ks);

                publicKeyContent = publicKeyContent.replaceAll("\\r|\\n","")
                        .replace("-----BEGIN PUBLIC KEY-----","")
                        .replace("-----END PUBLIC KEY-----","");

                /* Generate public key.*/
               
                X509EncodedKeySpec ks2 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));

                PublicKey pub = kf.generatePublic(ks2);


                //rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
//                System.out.println(rsaJsonWebKey.getKey().getFormat());
  //              System.out.println(rsaJsonWebKey.getPrivateKey().getFormat());


                rsaJsonWebKey = new RsaJsonWebKey((RSAPublicKey) pub);
               rsaJsonWebKey.setPrivateKey(pvt);
               rsaJsonWebKey.setKeyId(publicKeyContent);


            }

            catch (Exception ex){
                ex.printStackTrace();
            }
        }

        public static String generateJwtToken( String id ) throws JoseException {
            // Give the JWK a Key ID (kid), which is just the polite thing to do
            rsaJsonWebKey.setKeyId("k1");

            // Create the Claims, which will be the content of the JWT
            JwtClaims claims = new JwtClaims();
            // who creates the token and signs it
            claims.setIssuer( issuer );
            // time when the token will expire (timeToExpire minutes from now)
            claims.setExpirationTimeMinutesInTheFuture( timeToExpire );
            // a unique identifier for the token
            claims.setGeneratedJwtId();
            // when the token was issued/created (now)
            claims.setIssuedAtToNow();
            // time before which the token is not yet valid (2 minutes ago)
            claims.setNotBeforeMinutesInThePast(0);
            // transmit the user id for later authentication
            claims.setClaim( "id", id );

            // A JWT is a JWS and/or a JWE with JSON claims as the payload.
            // In this example it is a JWS so we create a JsonWebSignature object.
            JsonWebSignature jws = new JsonWebSignature();
            // The payload of the JWS is JSON content of the JWT Claims
            jws.setPayload( claims.toJson() );
            // The JWT is signed using the private key

            jws.setKey( rsaJsonWebKey.getPrivateKey() );

            // Set the Key ID (kid) header because it's just the polite thing to do.
            // We only have one key in this example but a using a Key ID helps
            // facilitate a smooth key rollover process
            jws.setKeyIdHeaderValue( rsaJsonWebKey.getKeyId() );

            // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
            jws.setAlgorithmHeaderValue( AlgorithmIdentifiers.RSA_USING_SHA256 );

            // Sign the JWS and produce the compact serialization or the complete JWT/JWS
            // representation, which is a string consisting of three dot ('.') separated
            // base64url-encoded parts in the form Header.Payload.Signature
            // If you wanted to encrypt it, you can simply set this jwt as the payload
            // of a JsonWebEncryption object and set the cty (Content Type) header to "jwt".
            String jwt = jws.getCompactSerialization();

            // Now you can do something with the JWT. Like send it to some other party
            return jwt;
        }

        public static String validateJwtToken( String jwt ) throws InvalidJwtException {
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    // the JWT must have an expiration time
                    .setRequireExpirationTime()
                    // but the  expiration time can't be too crazy
                    .setMaxFutureValidityInMinutes( timeToExpire )
                    // allow some leeway in validating time based claims to account for clock skew
                    .setAllowedClockSkewInSeconds( 30 )
                    // whom the JWT needs to have been issued by
                    .setExpectedIssuer( issuer )
                    // verify the signature with the public key
                    .setVerificationKey( rsaJsonWebKey.getKey() )
                    .build();

            //  Validate the JWT and process it to the Claims
            JwtClaims jwtClaims = jwtConsumer.processToClaims( jwt );
            System.out.println( "JWT validation succeeded! " + jwtClaims );

            // validate and return the encoded user id
            return jwtClaims.getClaimsMap().get("id").toString();
        }
    }
