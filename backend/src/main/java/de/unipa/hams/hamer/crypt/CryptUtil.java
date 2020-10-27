package de.unipa.hams.hamer.crypt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Slf4j
public class CryptUtil {
  private CryptUtil() {
  }

  public static String fourDigitPin() {
    try {
      SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
      while (true) {
        int i = secureRandom.nextInt(9999);
        if (i > 999) return String.valueOf(i);
      }
    } catch (NoSuchAlgorithmException e) {
      log.error(ExceptionUtils.getStackTrace(e));
      throw new IllegalArgumentException("Houston, we have a problem...", e);
    }
  }

}
