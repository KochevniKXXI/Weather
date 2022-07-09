package ru.nomad.weather;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * TODO: document your custom view class.
 */
public class ThermometerView extends View {
    // Константы
    // Отступ элементов
    private static final int padding = 10;

    // Цвет термометра
    private int thermometerColor = Color.GRAY;
    // Цвет колбы
    private int flaskColor = Color.WHITE;
    // Цвет ртути
    private int mercuryColor = Color.RED;
    // Изображение термометра
    private RectF thermometerRectangle = new RectF();
    // Изображение колбы
    private RectF flaskRectangle = new RectF();
    // Изображение ртутного столбика
    private Rect mercuryRectangle = new Rect();
    // "Краска" термометра
    private Paint thermometerPaint;
    // "Краска" шкалы
    private Paint scalePaint;
    // "Краска" колбы
    private Paint flaskPaint;
    private Paint glassPaint;
    private Paint glassRoundPaint;
    // "Краска" ртути
    private Paint mercuryPaint;
    // Ширина элемента
    private int width = 0;
    // Высота элемента
    private int height = 0;

    // Уровень ртутного столбика
    private int mercuryLevel = 0;
    private Paint glassTopPaint;
    private Paint glassEdgeRoundPaint;

    public ThermometerView(Context context) {
        super(context);
        init(null, 0);
    }

    // Вызывается при добавлении элемента в макет
    // AttributeSet attrs - набор параметров, указанных в макете для этого элемента
    public ThermometerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    // Вызывается при добавлении элемента в макет с установленными стилями
    // AttributeSet attrs - набор параметров, указанных в макете для этого элемента
    // int defStyle - базовый установленный стиль
    public ThermometerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    // Вызывается при добавлении элемента в макет с установленными стилями
    // AttributeSet attrs - набор параметров, указанных в макете для этого элемента
    // int defStyleAttrs - базовый установленный стиль
    // int defStyleRes - ресурс стиля, если он не был определён в предыдущем параметре
    public ThermometerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Инициализация атрибутов пользовательского элемента из xml
        // При помощи этого метода получаем доступ к набору атрибутов
        // На выходе - массив со значениями
        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ThermometerView, defStyle, 0);

        // Чтобы получить какое-либо значение из этого массива,
        // надо вызвать соответствующий метод и передать в этот метод имя
        // ресурса, указанного в файле определения атрибутов (attrs_....xml)

        thermometerColor = typedArray.getColor(R.styleable.ThermometerView_thermometerColor, Color.GRAY);

        // Вторым параметром идёт значение по умолчанию.
        // Оно будет подставлено, если атрибут не будет указан в макете
        mercuryColor = typedArray.getColor(R.styleable.ThermometerView_mercuryColor, Color.RED);
        mercuryLevel = typedArray.getInteger(R.styleable.ThermometerView_mercuryLevel, 0);
        typedArray.recycle();

        // Начальная инициализация полей класса
        thermometerPaint = new Paint();
        thermometerPaint.setColor(thermometerColor);
        thermometerPaint.setStyle(Paint.Style.FILL);
        scalePaint = new Paint();
        scalePaint.setColor(thermometerColor);
        scalePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        scalePaint.setStrokeWidth(5.0f);
        flaskPaint = new Paint();
        flaskPaint.setColor(flaskColor);
        flaskPaint.setStyle(Paint.Style.FILL);
        mercuryPaint = new Paint();
        glassPaint = new Paint();
        glassRoundPaint = new Paint();
        glassTopPaint = new Paint();
        glassEdgeRoundPaint = new Paint();
    }

    public void setMercuryLevel(int mercuryLevel) {
        this.mercuryLevel = mercuryLevel + 50;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Получаем реальные ширину и высоту
        width = w - getPaddingLeft() - getPaddingRight();
        height = h - getPaddingTop() - getPaddingBottom();
        // Отрисовка термометра
        thermometerRectangle.set(width / 4.0f + padding / 2.0f, padding, width * 3 / 4.0f - padding / 2.0f, height - width / 2.0f);
        flaskRectangle.set(width / 4.0f + padding, 2 * padding, width * 3 / 4.0f - padding, height - 2 * padding);
        mercuryPaint.setShader(new LinearGradient(0, 2 * padding, 0, height - 2 * padding, new int[] {Color.RED, Color.BLUE, Color.CYAN}, new float[] {0.35f, 0.6f, 1}, Shader.TileMode.MIRROR));
        glassPaint.setShader(new LinearGradient(width / 4.0f + padding, 0, width * 3 / 4.0f - padding, 0, new int[] {Color.WHITE, Color.TRANSPARENT, Color.WHITE, Color.TRANSPARENT}, new float[] {0, 0.5f, 0.75f, 1}, Shader.TileMode.MIRROR));
        glassTopPaint.setShader(new RadialGradient(width / 2.0f, 2 * padding, width / 4.0f - padding, Color.WHITE, Color.TRANSPARENT, Shader.TileMode.CLAMP));
        glassRoundPaint.setShader(new RadialGradient(width / 2.0f + padding,  height - width / 2.0f - padding, width / 4.0f, Color.WHITE, Color.TRANSPARENT, Shader.TileMode.CLAMP));
        glassEdgeRoundPaint.setShader(new RadialGradient(width / 2.0f,  height - width / 2.0f, width / 2.0f, Color.TRANSPARENT, Color.WHITE, Shader.TileMode.CLAMP));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mercuryRectangle.set(width / 4 + padding * 3 / 2, (height - 2 * padding) * (100 - mercuryLevel) / 100, width * 3 / 4 - padding * 3 / 2, height - 2 * padding);
        canvas.drawCircle(width / 2.0f, height - width / 2.0f, width / 2.0f - padding, thermometerPaint);
        canvas.drawRoundRect(thermometerRectangle, width / 4.0f - padding / 2.0f, width / 4.0f - padding / 2.0f, thermometerPaint);
//        canvas.drawCircle(width / 2.0f, height - width / 2.0f, width / 2.0f - 1.5f * padding, flaskPaint);
//        canvas.drawRoundRect(flaskRectangle, width / 4.0f - padding, width / 4.0f - padding, flaskPaint);
        canvas.drawRect(mercuryRectangle, mercuryPaint);
        canvas.drawRoundRect(flaskRectangle, width / 4.0f - padding, width / 4.0f - padding, glassPaint);
        canvas.drawRoundRect(flaskRectangle, width / 4.0f - padding, width / 4.0f - padding, glassTopPaint);
        canvas.drawCircle(width / 2.0f, height - width / 2.0f, width / 2.0f - 2 * padding, mercuryPaint);
        canvas.drawCircle(width / 2.0f, height - width / 2.0f, width / 2.0f - 1.5f * padding, glassRoundPaint);
        canvas.drawCircle(width / 2.0f, height - width / 2.0f, width / 2.0f - 1.5f * padding, glassEdgeRoundPaint);

        // Отрисовка шкалы
        canvas.drawText("40", padding, height * 0.09f, thermometerPaint);
        canvas.drawLine(padding, height * 0.1f, padding * 3, height * 0.1f, scalePaint);
        canvas.drawText("30", padding, height * 0.19f, thermometerPaint);
        canvas.drawLine(padding, height * 0.2f, padding * 3, height * 0.2f, scalePaint);
        canvas.drawText("20", padding, height * 0.29f, thermometerPaint);
        canvas.drawLine(padding, height * 0.3f, padding * 3, height * 0.3f, scalePaint);
        canvas.drawText("10", padding, height * 0.39f, thermometerPaint);
        canvas.drawLine(padding, height * 0.4f, padding * 3, height * 0.4f, scalePaint);
        canvas.drawText("0", padding, height * 0.49f, thermometerPaint);
        canvas.drawLine(padding, height * 0.5f, padding * 3, height * 0.5f, scalePaint);
        canvas.drawText("-10", padding / 2.0f, height * 0.59f, thermometerPaint);
        canvas.drawLine(padding, height * 0.6f, padding * 3, height * 0.6f, scalePaint);
        canvas.drawText("-20", padding / 2.0f, height * 0.69f, thermometerPaint);
        canvas.drawLine(padding, height * 0.7f, padding * 3, height * 0.7f, scalePaint);
        canvas.drawText("-30", padding / 2.0f, height * 0.79f, thermometerPaint);
        canvas.drawLine(padding, height * 0.8f, padding * 3, height * 0.8f, scalePaint);
    }
}