import org.junit.Assert;
import org.junit.Test;
import utils.VatCalculator;

public class VatCalculatorTest {
    private VatCalculator vatCalc = new VatCalculator();

    @Test
    public void checkIfVatCalculatedCorrect() {
        Assert.assertEquals(vatCalc.getVatAmountFromGross(5.0), 0.83, 0.01);
    }
}
