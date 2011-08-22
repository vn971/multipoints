package ru.narod.vn91.pointsop.utils;

/**
 * This is a functor.
 * You can find what that is in google/wiki
 * (I found it in Functional Programming and now wanna test)
 *
 * @author vasya
 *
 * @param <Input>
 *          input type
 * @param <Output>
 *          output type
 */
public interface Functor<Input, Output> {
	Output call(Input input);
}
