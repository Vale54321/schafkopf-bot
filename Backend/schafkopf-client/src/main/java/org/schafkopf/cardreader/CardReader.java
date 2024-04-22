package org.schafkopf.cardreader;

import java.util.concurrent.CountDownLatch;

/** Class that represents one Card Reader. */
public abstract class CardReader {

  private CountDownLatch nfcLatch = new CountDownLatch(1);
  private Boolean readingMode = false;
  private String uidString = "";

  public CardReader() {

  }

  /** method to call to wait for NFC input. */
  public String waitForCardScan() throws InterruptedException {
    this.readingMode = true;
    nfcLatch.await();
    Thread.sleep(20);
    this.readingMode = false;
    nfcLatch = new CountDownLatch(1);
    return this.uidString;
  }

  /**
   * checks uid of scanned card and do nothing if Server is not in reading mode.
   *
   * @param uidString uid to check.
   */
  public void nfcGelesen(String uidString) {
    if (this.uidString.equals(uidString)) {
      return;
    }
    if (!this.readingMode) {
      return;
    }

    this.uidString = uidString;
    nfcLatch.countDown();
  }
}
