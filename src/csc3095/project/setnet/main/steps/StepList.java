package csc3095.project.setnet.main.steps;

import java.util.ArrayList;
import java.util.List;

//http://www.java2novice.com/data-structures-in-java/linked-list/doubly-linked-list/
public class StepList<E>
{
    private Step head;
    private Step tail;
    private Step current;
    private int size;

    public StepList() { size = 0; }

    public int size() { return size; }

    public Step getCurrent() { return current; }

    public boolean isEmpty() { return size == 0; }

    public boolean isCurrentAtHead() { return current == head; }

    public boolean isCurrentAtTail() { return current == tail; }

    public void saveStep(E element)
    {
        Step newStep = new Step(element, tail, null);
        if (tail != null) tail.next = newStep;
        tail = newStep;
        current = newStep;

        if (head == null) head = newStep;
        ++size;
    }

    public E navForward()
    {
        current = current.next;
        E data = current.element;
        return data;
    }

    public E navBack()
    {
        current = current.previous;
        E data = current.element;
        return data;
    }

    public void clearFromPosition()
    {
        tail = current;
        Step temp = head;
        int counter = 0;
        while (temp != current)
        {
            temp = temp.next;
            ++counter;
        }
        size = counter;
    }

    public void clear()
    {
        head = null;
        tail = null;
        current = null;
        size = 0;
    }

    public List<E> getList()
    {
        List<E> newList = new ArrayList<>();
        Step temp = head;
        while (temp != tail)
        {
            newList.add(temp.element);
            temp = temp.next;
        }
        return newList;
    }

    public List<E> getListUntilCurrent()
    {
        List<E> newList = new ArrayList<>();
        Step temp = head;
        while (temp != current)
        {
            newList.add(temp.element);
            temp = temp.next;
        }
        newList.add(current.element);
        return newList;
    }

    private class Step
    {
        private E element;
        private Step previous;
        private Step next;

        public Step(E element, Step previous, Step next)
        {
            this.element = element;
            this.previous = previous;
            this.next = next;
        }

        @Override
        public String toString()
        {
            String previousE = "";
            String nextE = "";

            if (previous == null)   previousE = "null";
            else                            previousE = previous.element + "";

            if (next == null)       nextE = "null";
            else                            nextE = next.element + "";

            return previousE + "->" + element + "->" + nextE;
        }
    }
}