package com.jupiter.web.manager.utils;

import javax.crypto.Cipher;
import java.net.URLDecoder;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtil {

    private static Map<Integer, String> keyMap = new HashMap<Integer, String>(); //用于封装随机产生的公钥与私钥

    // 解密测试服务器返回给客户端的参数
    private static String testTestClient(String file) throws Exception {
        String key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMPbpgkR1qiyKHP2pkLko0wsyO0A6AB6z7H73IwLsIw2Pvln+dbwQQWDa5wQuTThfoPtN/5O+RB6JlcLjPNTwwEPai/G2z5AerqZuSXr0zs+kEGkUiLFFv9uOV32+tsDF0HpifN9OPUsY9WZvCuwX/mdqsW69Up5mSDerYdquPvXAgMBAAECgYBJgWHq/XrmjRaJjwQPDugKarRt3m0z4OOaFMBM/wYfWSyBRfiKJlkAzLXSOKSJqCdnM9xT4Sewe2dwO7GGkOUaE4n1Ne0HudJv6KPaobW1pTG6jcNJ2/kmMZjiYxOnofGKx8uU1bq4UeolpXeR9ZbqC03XQ0kmqJx4/QN2X3zB4QJBAPumI/Db4Z9tZ4Pc3nFt0xFtnv7i0A0+SWDxUNymvl9YY0mfDWyy2djIOuaWaYMl6a9awCOY2NDxtsyjvPQtAK0CQQDHPpBQCqCLTBa6OqsWzpP8Q9G8+tao/nMT6SDrow+iMWTVuWQIHmDOG4E5e9xvVKC05vSijldpbDzM1wFYlIsTAkBPErB+cYke4HDDdXaMK9dDxa9NJhX741OJW6pt+xMcwJhKPOkp+7UKMjiX1jGHXTVlvV1ckh2m4hzvho/MJgvtAkEAlEzRBYKjdIsx73NznQotw/qGxm6+EBM0fIFta3Z3UHYLY7kV56UU0NRQHwU+Ue9fnsa5F7VzApj5uE3gRppp2QJAOfYux9Xpr67wVoSlnDzWRIlrhNQAsWA3MnHll1JC5SVRbWuah2Jvdt8P635cs7oxLHn0gwxom2xbULDQ1qYD8Q==";
        String aeskey = decrypt(file, key);
        System.out.println(aeskey);

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDD26YJEdaosihz9qZC5KNMLMjtAOgAes+x+9yMC7CMNj75Z/nW8EEFg2ucELk04X6D7Tf+TvkQeiZXC4zzU8MBD2ovxts+QHq6mbkl69M7PpBBpFIixRb/bjld9vrbAxdB6YnzfTj1LGPVmbwrsF/5narFuvVKeZkg3q2Harj71wIDAQAB";

        file = encrypt(aeskey, publicKey);
        System.out.println(file);

        aeskey = decrypt(file, key);
        System.out.println(aeskey);
        return aeskey;
    }

    // 解密生产服务器返回给客户端的参数
    private static String testProductClient(String file) throws Exception {
        String key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMClsDY7tESKqgzhQQORavL/h/R26i0bMOn9a/atZYqzjNE1xLzs6ghzR8xGlq7+VhhMK97t74J2ZvQa0IiRwJORngdd1att/nWThykyHLM9UYplwOKPbgU9t0uzuS1V/1r9rLsc95r1XXPvSznngX7fNRxiYNsmqWxFDwlkGw0DAgMBAAECgYBu8j0UTrgXzT/34PYfRu0v+ufia2WcCo/WAiPeUagsBBGyHuQp5cs2iKWtyG1ZZJJStfOCFMed+/B43sSAEmiz0fPk0q4xZU8rRBj3Ryx4+H1QQZjJnYcWbahJCa9q9/s8vLFINg8SB0Dy3kY3YodJ/9mo95H5vcHPzpl41yc5CQJBAPzSjCNzTLtwz2qbMW3kGPgpKnXep0DcZg9AM4UsEWs9pOcuaPVc8lJm01LUnKt2flo+APX9ywXO9kaWlBA3lzUCQQDDEYcpaDIcfRvg+5KDX44FVJfOj1hvwHvfVwqaYTT2fwTNPyyrzdqksXFf9gCvNzcH+AtcQFOcgxbEyddTr0JXAkEA648o7ybQiZx0RTnP9jmeYinI3AXWBALWPMNuvVfv6uGGsbd2J5awWMJhhz19s+w1kdNPheMvtHu+zsohrnJ9OQJAHe2nhiPVZGeRinAup3dWq9yxueBmWib3GmoZV1xIqvUMAI2LxgQ8Yn5S/b0Zl8hp6hAVdh7sdRvrt1+caNt/2wJBALo/rTuKB5PBEs0UW5N7KgoGBrJuQgHNxQwquNSuD/Kf/jHAe+hGV6GpzH+j08DnT+BTlZ0uhkR5meJeI8dJX0o=";
        String aeskey = decrypt(file, key);
        System.out.println(aeskey);

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDApbA2O7REiqoM4UEDkWry/4f0duotGzDp/Wv2rWWKs4zRNcS87OoIc0fMRpau/lYYTCve7e+Cdmb0GtCIkcCTkZ4HXdWrbf51k4cpMhyzPVGKZcDij24FPbdLs7ktVf9a/ay7HPea9V1z70s554F+3zUcYmDbJqlsRQ8JZBsNAwIDAQAB";

        file = encrypt(aeskey, publicKey);
        System.out.println(file);

        aeskey = decrypt(file, key);
        System.out.println(aeskey);
        return aeskey;
    }

    // 解密客户端发给生产服务器的参数
    private static String testProductServer(String file) throws Exception {
        String key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALYs2UJPD/Iacz6yNTwy6MxKrgr0jzTJ4Amx0YtGqeTEWtK4EES8a9FVwWBSrSVKxlG9yo4x38UKurkyRh874F9pkibPoe0ZJK/90LLLAAlMJov+oqHYr6SrENrmRLk8L44l4IgdTG8QwEcxQeraTMQ/MIOla0FFEOUAVSZmwrDJAgMBAAECgYAsrbkm6w8FGHmVMc2ekvnHSLWAmFQa5TveBM9Ych8urP19Woka4n8q2vZPMlemor0axQpNb2vq/mOnEe5isKOpOORD29sO9eh1leqBx5nnZ83yaUa1Q6sJSZEa/6BuLyvtC7wvYWTOMxjEm6x0OLGN1Y/qITDHOtt6l4inmQqytQJBANi9ebRKxR0zrlM6yvq6P7l6w5hunaliN97E/+wbZCq+Mz8g4oYv7OL2OZ9AUYzMwn2pYAluUVt5ClRwC1JLwicCQQDXLI0fb2J0M7feea/oKyw3Lcc7pLm38C/l8vCkg2uYgTst9L8pDPAkiTew65Kwx1ZDuM4weuJArCuZGMWeY/uPAkBYUg3QLE/asH6vJ9wjA9aH7MDb0ueoaH+/5aMsQZU13b/gT7Q/XjQ34IJmpL0WlTIEJwsdjLjWf6J3+hiC9qAtAkBcJcYh6i99mBBsBCzi/monD0G9NyQZs5Cvp0SLD4TqgJ15ZtDeoZ3N572j1J6zegplYiVa+aUfERxyqfsng6XdAkEAyKRhY5yzGPh/Sw6kzCwlmsj0XB0lgMFEvdPj2CZlA2mQTAUAkf/SSqCWAUD9ZIqOsXI/1Lk9QYMNTpKMBxbgxw==";
        String aeskey = decrypt(file, key);
        System.out.println(aeskey);

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2LNlCTw/yGnM+sjU8MujMSq4K9I80yeAJsdGLRqnkxFrSuBBEvGvRVcFgUq0lSsZRvcqOMd/FCrq5MkYfO+BfaZImz6HtGSSv/dCyywAJTCaL/qKh2K+kqxDa5kS5PC+OJeCIHUxvEMBHMUHq2kzEPzCDpWtBRRDlAFUmZsKwyQIDAQAB";

        file = encrypt(aeskey, publicKey);
        System.out.println(file);

        aeskey = decrypt(file, key);
        System.out.println(aeskey);
        return aeskey;
    }

    // 解密客户端发给测试服务器的参数
    private static String testTestServer(String file) throws Exception {
        String key = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALvtHu5eh5RtvfCokWkIFsbE8MckFL6ArFHJDe8kH9oGvS+pILvFntgCiscYNkdFGNVs5Ludpl4CkU3XSRX6JFiyRi22jdl0RUj0SW/SQOhvAmcamQz8Ane/rsGXy71PfnCSqN0NHgr8YGoZSATIhXSld+l794AiMRQq00A9anclAgMBAAECgYA7R4wnQxhfgNKWlIXx7MRKhRsypjYI9cvlrJdBpVKK0u9glmgl8yutFfgy33dXufsgb84jRJKTOxocxNlgHlzSNqcYVkKBP2GycHQWX03Vio5+NjYIuuzehqlHADFTL/mAHKeTQRmmGzZq02YpD09NVtqmOAoHrtXgUb+kAozPjQJBAPMC9cam6ExxUWris1JfoUp7/L9xEWgWz54ogvX1TH6EzZtnxqt/Hw4sANyQ459KD+EtQq2mCvK8c6ldpei9VE8CQQDF+HLA306//qRbMVRNNHiI6oM2iSSRKf0caZs0NEn9rsdIvYDrPNS2qL1FxIjzu04ad5b1i6AlC/LyLDXDe/xLAkAXYQSGLQ6SNCCyGMdJuHuK7+y7+kPDcNZej+UxhnzqexpQxiYd36f8NA2NKr9LVtO6RSFyFVReT/UMqT7J6lUPAkAvDqFoSyxS4tp6er1JJMHJ+cR6wBqbDtoFWOz1ecBktpjUJ4dvYyREztM+tDrBhLJKBEcz0p6cb7yt5+hXsS8FAkBGyBol94Phz2Ej1vR3/FBwKAkpKpsxFEQQqWdV0fOiqJxh60/ar2MurYd3Wg5clP+fb70O8rIbc9a/yckNb9an";
        String aeskey = decrypt(file, key);
        System.out.println(aeskey);

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC77R7uXoeUbb3wqJFpCBbGxPDHJBS+gKxRyQ3vJB/aBr0vqSC7xZ7YAorHGDZHRRjVbOS7naZeApFN10kV+iRYskYtto3ZdEVI9Elv0kDobwJnGpkM/AJ3v67Bl8u9T35wkqjdDR4K/GBqGUgEyIV0pXfpe/eAIjEUKtNAPWp3JQIDAQAB";

        file = encrypt(aeskey, publicKey);
        System.out.println(file);

        aeskey = decrypt(file, key);
        System.out.println(aeskey);
        return aeskey;
    }

    public static void main(String[] args) throws Exception {
        String key = "r/ngc6UzzyqN2jAbQbwn1q2VzCssGPme7lhhQmROEgd8zPV0PmGfvsygcawyHgBM+ESc1WsxTflf\n" +
                "+1MchjOMUqIF6iO19glGGeBT0Ot7Y236QdXptnY2EK/DRqtxhHbcX75a0/2CsO/dUlWRmHLb/MWM\n" +
                "GuhY5K/iWojCidsKHKU=";
        //key = URLDecoder.decode(key, "UTF-8");
        String aesKey = testTestClient(key);

        String param = "i1m0f2EW6EsUU2SeiDZduHGpTTzR4xEMQSnfYQvL5yVFIkv489nII2GYndoEF0IS4KgbVjKAs3qk\n" +
                "sdiddzqBEoZtmH91uS7RJmcEbKV+tdrs9ZusJvwSIH/yFkfX40DXHu0bU6BTyd4paOzyOjTQhCdu\n" +
                "n+grmepe4hYjN16Djz8riuWoiwg8iXir3hmUt+H+7HM+aQPqIk36jvk+xDuBEnjv8HoWIcbd4P3V\n" +
                "EOMM6V5fR16h8CYAmjHcsFRfjPPYYVTd8HUe7q2Z9/Es6yG0SLWyNgmzsy/NaO2+SE2z7g9r2Oa5\n" +
                "q2worT+zRyVOYIIqng1GU50L5rSzMrhR/ninznkn6Piik+m5NbSS1U2xJDmX07gWOpX9chEA0sPx\n" +
                "V3g/JmL/vljw30dKuaCudMtKnKiQ3OYSVR3zUKY+sYmSsZGRf8s0OxDDIOqpJLLectQp8RDRPcyC\n" +
                "VinD4teCobEFYw4u5NPWFIt+fD60x+AkVlsVsv3w+cHTodhQPeXVTHwC";
        //param = URLDecoder.decode(param, "UTF-8");
        param = new String(AESUtil.decrypt(aesKey.getBytes(), aesKey.getBytes(), Base64.decodeToBytes(param)));
        System.out.println(param);


        //生成公钥和私钥
//        genKeyPair();
//
//        String publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2LNlCTw/yGnM+sjU8MujMSq4K9I80yeAJsdGLRqnkxFrSuBBEvGvRVcFgUq0lSsZRvcqOMd/FCrq5MkYfO+BfaZImz6HtGSSv/dCyywAJTCaL/qKh2K+kqxDa5kS5PC+OJeCIHUxvEMBHMUHq2kzEPzCDpWtBRRDlAFUmZsKwyQIDAQAB";
//        String privatekey = "MIICXAIBAAKBgQC2LNlCTw/yGnM+sjU8MujMSq4K9I80yeAJsdGLRqnkxFrSuBBEvGvRVcFgUq0lSsZRvcqOMd/FCrq5MkYfO+BfaZImz6HtGSSv/dCyywAJTCaL/qKh2K+kqxDa5kS5PC+OJeCIHUxvEMBHMUHq2kzEPzCDpWtBRRDlAFUmZsKwyQIDAQABAoGALK25JusPBRh5lTHNnpL5x0i1gJhUGuU73gTPWHIfLqz9fVqJGuJ/Ktr2TzJXpqK9GsUKTW9r6v5jpxHuYrCjqTjkQ9vbDvXodZXqgceZ52fN8mlGtUOrCUmRGv+gbi8r7Qu8L2FkzjMYxJusdDixjdWP6iEwxzrbepeIp5kKsrUCQQDYvXm0SsUdM65TOsr6uj+5esOYbp2pYjfexP/sG2QqvjM/IOKGL+zi9jmfQFGMzMJ9qWAJblFbeQpUcAtSS8InAkEA1yyNH29idDO33nmv6CssNy3HO6S5t/Av5fLwpINrmIE7LfS/KQzwJIk3sOuSsMdWQ7jOMHriQKwrmRjFnmP7jwJAWFIN0CxP2rB+ryfcIwPWh+zA29LnqGh/v+WjLEGVNd2/4E+0P140N+CCZqS9FpUyBCcLHYy41n+id/oYgvagLQJAXCXGIeovfZgQbAQs4v5qJw9BvTckGbOQr6dEiw+E6oCdeWbQ3qGdzee9o9Ses3oKZWIlWvmlHxEccqn7J4Ol3QJBAMikYWOcsxj4f0sOpMwsJZrI9FwdJYDBRL3T49gmZQNpkEwFAJH/0kqglgFA/WSKjrFyP9S5PUGDDU6SjAcW4Mc=";
//        String privatekey8 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALYs2UJPD/Iacz6yNTwy6MxKrgr0jzTJ4Amx0YtGqeTEWtK4EES8a9FVwWBSrSVKxlG9yo4x38UKurkyRh874F9pkibPoe0ZJK/90LLLAAlMJov+oqHYr6SrENrmRLk8L44l4IgdTG8QwEcxQeraTMQ/MIOla0FFEOUAVSZmwrDJAgMBAAECgYAsrbkm6w8FGHmVMc2ekvnHSLWAmFQa5TveBM9Ych8urP19Woka4n8q2vZPMlemor0axQpNb2vq/mOnEe5isKOpOORD29sO9eh1leqBx5nnZ83yaUa1Q6sJSZEa/6BuLyvtC7wvYWTOMxjEm6x0OLGN1Y/qITDHOtt6l4inmQqytQJBANi9ebRKxR0zrlM6yvq6P7l6w5hunaliN97E/+wbZCq+Mz8g4oYv7OL2OZ9AUYzMwn2pYAluUVt5ClRwC1JLwicCQQDXLI0fb2J0M7feea/oKyw3Lcc7pLm38C/l8vCkg2uYgTst9L8pDPAkiTew65Kwx1ZDuM4weuJArCuZGMWeY/uPAkBYUg3QLE/asH6vJ9wjA9aH7MDb0ueoaH+/5aMsQZU13b/gT7Q/XjQ34IJmpL0WlTIEJwsdjLjWf6J3+hiC9qAtAkBcJcYh6i99mBBsBCzi/monD0G9NyQZs5Cvp0SLD4TqgJ15ZtDeoZ3N572j1J6zegplYiVa+aUfERxyqfsng6XdAkEAyKRhY5yzGPh/Sw6kzCwlmsj0XB0lgMFEvdPj2CZlA2mQTAUAkf/SSqCWAUD9ZIqOsXI/1Lk9QYMNTpKMBxbgxw==";
//        keyMap.put(0, publickey);
//        //0表示公钥
//        keyMap.put(1, privatekey8);
//
//        // 加密字符串
//        String message = "df723820";
//        System.out.println("随机生成的公钥为:" + keyMap.get(0));
//        System.out.println("随机生成的私钥为:" + keyMap.get(1));
//        String messageEn = encrypt(message, keyMap.get(0));
//        System.out.println(message + "\t加密后的字符串为:" + messageEn);
//        String messageDe = decrypt(messageEn, keyMap.get(1));
//        System.out.println("还原后的字符串为:" + messageDe);
//
//        String key = "FKvqABy5MUY1myaETzD48cMNiB2P74GJZSxMSbGrNsEvN4tQ%2BytXEjIeZiL4v4hoD97wh3BAWHPY%0AkHUTheTzjIDO7EgoBjnpEjED5wuDIfJyyWt0%2BlzHZSJDo1ft%2F8gKYMyYb4AP7Hez4G85%2BeXSMxS2%0AwKQHSSYp%2B0T8LV4wGKA%3D%0A";
//        String testKey = "FKvqABy5MUY1myaETzD48cMNiB2P74GJZSxMSbGrNsEvN4tQ+ytXEjIeZiL4v4hoD97wh3BAWHPYkHUTheTzjIDO7EgoBjnpEjED5wuDIfJyyWt0+lzHZSJDo1ft/8gKYMyYb4AP7Hez4G85+eXSMxS2wKQHSSYp+0T8LV4wGKA=";
//
//        String keyWord = URLDecoder.decode(key, "UTF-8");
//        System.out.println(keyWord.equals(testKey));
//        // 将普通字符创转换成application/x-www-from-urlencoded字符串
////        String urlString = URLEncoder.encode(keyWord, "UTF-8");
////        System.out.println(urlString.equals(key));
//
//        String messageDe1 = decrypt(keyWord, keyMap.get(1));
//        System.out.println("还原后的字符串为key1:" + messageDe1);
//        String messageDe2 = decrypt(testKey, keyMap.get(1));
//        System.out.println("还原后的字符串为key2:" + messageDe2);
//        System.out.println(messageDe2.length());
//
//        String param = "SXsD1AMPUGceFipqNzlTlnYl%2BKTuH%2FcJ2IgdTWOC%2FAkMEMhRkA%2BmR2ttiWdWRJgD7mwW90T9Po4U%0AyW528eL0DOOjiewjSw1F6tVchhjyXZNzoVQMEMNsI4AB9xBsOkt3lb1D9RcS%2B3NWOqpQuJaw5ZvL%0Anx6m1DjZXrDMS2rB%2F9g1jLymU8rvodPi1KKmoKYg46qNl9dOQWxiCxYc3Tt1Q4YHwRcwXOamnzfG%0ANZiWAdg%3D%0A";
//        param = URLDecoder.decode(param, "UTF-8");
//        param = new String(AESUtil.decrypt(messageDe2.getBytes(),messageDe2.getBytes(),Base64.decodeToBytes(param)));
//        System.out.println(param);
//       // param = TestAES2.invokeDecryptEncode(Base64.decode(param));
//       // param = TestAES2.invokeDecryptEncode(Base64.decode(param));
//       // System.out.println("s:  " + param);

    }

    /**
     * 随机生成密钥对
     *
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        //  初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 得到公钥
        String publicKeyString = new String(Base64.encode(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encode((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put(0, publicKeyString);
        //0表示公钥
        keyMap.put(1, privateKeyString);
        //1表示私钥
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */

    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeToBytes(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = new String(Base64.encode(cipher.doFinal(str.getBytes("UTF-8"))));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decode(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeToBytes(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }
}
