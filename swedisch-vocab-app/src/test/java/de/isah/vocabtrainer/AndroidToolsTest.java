package de.isah.vocabtrainer;

import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


import static org.junit.Assert.*;

public class AndroidToolsTest {

    @Test
    public void testGetDisplaySize(){
        assertEquals(111, AndroidTools.getDisplayDimensions(new TestWindowManager()).x);
        assertEquals(222, AndroidTools.getDisplayDimensions(new TestWindowManager()).y);
    }

    @Test
    public void testAddEmptyLinesFive(){
        String input = "one\ntwo\nthree\nfour\nfive";
        assertEquals(input, AndroidTools.addEmptyLines(input));
    }

    @Test
    public void testAddEmptyLinesThree(){
        String input = "one\ntwo\nthree";
        String output = "one\ntwo\nthree\n \n ";
        assertEquals(output, AndroidTools.addEmptyLines(input));
    }

    @Test
    public void testAddEmptyLinesOne(){
        String input = "one";
        String output = "one\n \n \n \n ";
        assertEquals(output, AndroidTools.addEmptyLines(input));
    }

    @Test
    public void testAddEmptyLinesSix(){
        String input = "one\ntwo\nthree\nfour\nfive\nsix";
        assertEquals(input, AndroidTools.addEmptyLines(input));
    }

    private class TestWindowManager implements WindowManager {

        @Override
        public Display getDefaultDisplay() {
            Display display = Mockito.mock(Display.class);
            Mockito.doAnswer(new Answer<Void>() {
                @Override
                public Void answer(InvocationOnMock invocation) throws Throwable {
                    Object[] args = invocation.getArguments();
                    Point testPoint = (Point) args[0];
                    testPoint.x = 111;
                    testPoint.y = 222;

                    return null;
                }
            }).when(display).getSize(Mockito.any(Point.class));

            return display;
        }

        @Override
        public void removeViewImmediate(View view) {

        }

        @Override
        public void addView(View view, ViewGroup.LayoutParams params) {

        }

        @Override
        public void updateViewLayout(View view, ViewGroup.LayoutParams params) {

        }

        @Override
        public void removeView(View view) {

        }
    }
}