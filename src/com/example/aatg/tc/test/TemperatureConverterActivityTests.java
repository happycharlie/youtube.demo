package com.example.aatg.tc.test;

import static android.test.ViewAsserts.assertLeftAligned;
import static android.test.ViewAsserts.assertOnScreen;
import static android.test.ViewAsserts.assertRightAligned;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aatg.tc.EditNumber;
import com.example.aatg.tc.TemperatureConverter;
import com.example.aatg.tc.TemperatureConverterActivity;

public class TemperatureConverterActivityTests extends
		ActivityInstrumentationTestCase2<TemperatureConverterActivity> {
	
	private TemperatureConverterActivity mActivity;
	private EditNumber mCelsius;
	private EditNumber mFahrenheit;
	private TextView mCelsiusLabel;
	private TextView mFahrenheitLabel;	

	public TemperatureConverterActivityTests(){
		this("TemperatureConverterActivityTests");
	}
	public TemperatureConverterActivityTests(String name) {
		super(TemperatureConverterActivity.class);
		setName(name);
	}

	protected void setUp() throws Exception {
		
		super.setUp();
		mActivity = getActivity();
		mCelsius = (EditNumber)
				mActivity.findViewById(com.example.aatg.tc.R.id.celsius);
		mFahrenheit = (EditNumber)
				mActivity.findViewById(com.example.aatg.tc.R.id.fahrenheit);
		mCelsiusLabel = (TextView) 
				mActivity.findViewById(com.example.aatg.tc.R.id.celsius_label);
		mFahrenheitLabel = (TextView) 
				mActivity.findViewById(com.example.aatg.tc.R.id.fahrenheit_label);
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public final void testPreconditions(){
		assertNotNull(mActivity);
	}

	public final void testHasInputFields(){
		assertNotNull(mCelsius);
		assertNotNull(mFahrenheit);
	}
	
	public final void testFieldsShouldStartEmpty(){
		assertEquals("",mCelsius.getText().toString());
		assertEquals("",mFahrenheit.getText().toString());
	}
	
	public final void testFieldsOnScreen(){
		final Window window = mActivity.getWindow();
		final View origin = window.getDecorView();
		assertOnScreen(origin, mCelsius);
		assertOnScreen(origin, mFahrenheit);
	}
	
	public final void testAlignment(){
		assertLeftAligned(mCelsiusLabel, mCelsius);
		assertLeftAligned(mFahrenheitLabel, mFahrenheit);
		assertLeftAligned(mCelsius, mFahrenheit);
		assertRightAligned(mCelsius, mFahrenheit);
	}
	
	public final void testCelsiusInputFieldCoverEntireScreen(){
		final int expected = LayoutParams.MATCH_PARENT;
		final LayoutParams lp = mCelsius.getLayoutParams();
		assertEquals("mCelsius layout width is not MATCH_PARENT", expected, lp.width);		
	}
	
	public final void testFahrenheitInputFieldCoverEntireScreen(){
		final int expected = LayoutParams.MATCH_PARENT;
		final LayoutParams lp = mFahrenheit.getLayoutParams();
		assertEquals("mFahrenheit layout width is not MATCH_PARENT", expected, lp.width);				
	}
	
	public final void testFontSizes(){
		final float expected = 24.0f;
		assertEquals(expected, mCelsiusLabel.getTextSize());
		assertEquals(expected, mFahrenheitLabel.getTextSize());
	}
	
	public final void testMargins(){
		LinearLayout.LayoutParams lp;
		final int expected = 6;
		lp = (LinearLayout.LayoutParams) mCelsius.getLayoutParams();
		assertEquals(expected, lp.leftMargin);		
		assertEquals(expected, lp.rightMargin);
		lp = (LinearLayout.LayoutParams) mFahrenheit.getLayoutParams();
		assertEquals(expected, lp.leftMargin);
		assertEquals(expected, lp.rightMargin);		
	}
	
	public final void testVirtualKeyboardSpaceReserved(){
		final int expected = 280;
		final int actual = mFahrenheit.getBottom();
		assertTrue(actual <= expected);
	}
	
	@UiThreadTest
	public final void testFahrenheitToCelsiusConversion(){
		mCelsius.clear();
		mFahrenheit.clear();
		
		final double f = 32.5;
		mFahrenheit.requestFocus();
		mFahrenheit.setNumber(f);
		mCelsius.requestFocus();
		final double expectedC = TemperatureConverter.fahrenheitToCelsius(f);
		Log.d("expectedC", "expectedC:************ " + expectedC );
		Log.d("EditNumber", "getNumber() returning value of '" + expectedC + "'");
		final double actualC = mCelsius.getNumber();
		//final double actualC = 100.3;
		final double delta = Math.abs(expectedC - actualC);
		final String msg = " " + f + "F -> " + expectedC + "C "
				+ "but was " + actualC + "C (delta " + delta + ")";
		assertTrue(msg, delta < 0.005);
	}
	
	public void testInputFilter() throws Throwable {
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				mCelsius.requestFocus();
			}
		});
		final Double n = -1.234d;
		sendKeys("MINUS 1 PERIOD 2 PERIOD 3 PERIOD 4");
		Object nr = null;
		try{
			nr = mCelsius.getNumber();
		}
		catch (NumberFormatException e) {
			nr = mCelsius.getText();
		}
		final String msg = "-1.2.3.4 should be filtered to " + n + 
				" but is " + nr;
		assertEquals(msg, n, nr);
		
		
		
	}
}
