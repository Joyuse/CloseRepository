package com.example.vladimir.sityinfov113;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Vladimir on 16.08.2017.
 */

public class OpenGLProjectRenderer implements GLSurfaceView.Renderer {

    //вершинный буфер
    final FloatBuffer verticesReady;

    int colored_vertices_program_handle;
    int MVPmatrix_location;
    int mPositionHandle;
    int mColorHandle;
    final int mBytesPerFloat = 4;
    final int mStrideBytes = 7 * mBytesPerFloat;
    final int mPositionOffset = 0;
    final int mPositionDataSize = 3;
    final int mColorOffset = 3;
    final int mColorDataSize = 4;


    public Camera camera = new Camera();


    FloatBuffer test_vertices;

    public OpenGLProjectRenderer() {
        float[] vertices = {
                //Координаты XYZ
                //ЦВЕТ RGB

                // треугольник 1
                -0.9f, 0.8f,0.0f,
                1.0f, 1.0f, 1.0f, 0.0f,

                -0.9f, 0.2f,0.0f,
                1.0f, 1.0f, 1.0f, 0.0f,

                -0.5f, 0.8f,0.0f,
                1.0f, 1.0f, 1.0f, 0.0f,

                //боковина Розовая
                -0.5f, 0.8f,1.0f,
                1.0f, 0.5f, 0.5f, 0.0f,

                -0.5f, 0.8f,0.0f,
                1.0f, 0.5f, 0.5f, 0.0f,

                -0.9f, 0.2f,1.0f,
                1.0f, 0.5f, 0.5f, 0.0f,

                //боковниа серая
                -0.9f, 0.2f,1.0f,
                0.5f, 0.5f, 0.5f, 0.0f,

                -0.9f, 0.2f,0.0f,
                0.5f, 0.5f, 0.5f, 0.0f,

                -0.5f, 0.8f,0.0f,
                0.5f, 0.5f, 0.5f, 0.0f,

                //боковина темно-зеленая
                -0.9f, 0.2f,1.0f,
                0.6f, 0.6f, 0.0f, 0.0f,

                -0.9f, 0.2f,0.0f,
                0.6f, 0.6f, 0.0f, 0.0f,

                -0.9f, 0.8f,0.0f,
                0.6f, 0.6f, 0.0f, 0.0f,

                //Боковина тоже ярко зеленого цвета
                -0.9f, 0.8f,1.0f,
                0.2f, 0.8f, 0.0f, 0.0f,

                -0.9f, 0.8f,0.0f,
                0.2f, 0.8f, 0.0f, 0.0f,

                -0.5f, 0.8f,0.0f,
                0.2f, 0.8f, 0.0f, 0.0f,

                //боковина хз какого цвета
                -0.9f, 0.8f,1.0f,
                0.3f, 0.8f, 0.0f, 0.0f,

                -0.5f, 0.8f,0.0f,
                0.3f, 0.8f, 0.5f, 0.0f,

                -0.5f, 0.8f,1.0f,
                0.3f, 0.8f, 0.5f, 0.0f,

                //другая сторона синяя
                -0.9f, 0.8f,1.0f,
                0.1f, 0.4f, 0.7f, 0.0f,

                -0.9f, 0.8f,0.0f,
                0.1f, 0.4f, 0.5f, 0.0f,

                -0.9f, 0.2f,1.0f,
                0.1f, 0.4f, 0.7f, 0.0f,

                //крыша в последнюю очередь
                -0.9f, 0.8f, 1.0f,
                1.0f, 0.5f, 1.0f, 0.0f,

                -0.9f, 0.2f, 1.0f,
                1.0f, 0.5f, 1.0f, 0.0f,

                -0.5f, 0.8f, 1.0f,
                1.0f, 0.5f, 1.0f, 0.0f,

                // треугольник 2
                -0.6f, 0.2f,0.0f,
                0.34f, 1.0f, 1.0f, 0.0f,

                -0.2f, 0.2f,0.0f,
                1.0f, 0.0f, 1.0f, 0.0f,

                -0.2f, 0.8f,0.0f,
                1.0f, 0.0f, 1.0f, 0.0f,

                // треугольник 3
                0.1f, 0.8f,0.0f,
                0.34f, 1.0f, 1.0f, 0.0f,

                0.1f, 0.2f,0.0f,
                0.34f, 1.0f, 1.0f, 0.0f,

                0.5f, 0.8f,0.0f,
                0.34f, 1.0f, 1.0f, 0.0f,

                // треугольник 4
                0.1f, 0.2f,0.0f,
                0.34f, 0.2f, 1.0f, 0.0f,

                0.5f, 0.2f,0.0f,
                0.34f, 0.2f, 1.0f, 0.0f,

                0.5f, 0.8f,0.0f,
                0.34f, 0.2f, 1.0f, 0.0f,

                //уже не какая-то поебота
                1.0f,-1.0f,0.0f,
                0.34f, 0.5f, 0.5f, 0.0f,
                1.0f,-1.5f,0.0f,
                0.34f, 0.5f, 0.5f, 0.0f,
                1.5f,-1.0f,0.0f,
                0.34f, 0.5f, 0.5f, 0.0f,
                1.5f,-1.5f,0.0f,
                0.34f, 0.5f, 0.5f, 0.0f,

        };

        float[] test={
            5f,5f,0f,
            1f, 1f, 1f, 1f,
            10f,10f,0f,
            1f, 0f, 0f,1f,
            15f,5f,0f,
            1f, 1f, 1f, 1f,
        };

        test_vertices = ByteBuffer.allocateDirect(test.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        test_vertices.put(test).position(0);

        verticesReady = ByteBuffer.allocateDirect(vertices.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
        verticesReady.put(vertices).position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //Вершинный шейдер
        final String vertexShader =
                "uniform mat4 u_MVPMatrix;      \n"
                        + "attribute vec4 a_Position;     \n"
                        + "attribute vec4 a_Color;        \n"
                        + "varying vec4 v_Color;          \n"
                        + "void main()                    \n"
                        + "{                              \n"
                        + "   v_Color = a_Color;          \n"
                        + "   gl_Position = u_MVPMatrix   \n"
                        + "               * a_Position;   \n"
                        + "}                              \n";

        //Фрагментный шейдер
        final String fragmentShader =
                "precision mediump float;       \n"
                        + "varying vec4 v_Color;          \n"
                        + "void main()                    \n"
                        + "{                              \n"
                        + "   gl_FragColor = v_Color;     \n"
                        + "}                              \n";

        //считыватель шейдера вершинного
        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        //создание вершинного шейдера
        if (vertexShaderHandle != 0)
        {
            GLES20.glShaderSource(vertexShaderHandle, vertexShader);

            GLES20.glCompileShader(vertexShaderHandle);

            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            if (compileStatus[0] == 0)
            {
                GLES20.glDeleteShader(vertexShaderHandle);
                vertexShaderHandle = 0;
            }
        }

        //Проверка на ошибку вершинного шейдера
        if (vertexShaderHandle == 0)
        {
            throw new RuntimeException("Error creating vertex shader.");
        }

        //считывание фрагментного шейдера
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        //создание фрагментного шейдера
        if (fragmentShaderHandle != 0)
        {
            GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);
            GLES20.glCompileShader(fragmentShaderHandle);
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            if (compileStatus[0] == 0)
            {
                GLES20.glDeleteShader(fragmentShaderHandle);
                fragmentShaderHandle = 0;
            }
        }

        //Проверка на фрагментного шейдера
        if (fragmentShaderHandle == 0)
        {
            throw new RuntimeException("Error creating fragment shader.");
        }

        //Создание программы из шейдеров
        colored_vertices_program_handle = GLES20.glCreateProgram();

        if (colored_vertices_program_handle != 0)
        {
            GLES20.glAttachShader(colored_vertices_program_handle, vertexShaderHandle);
            GLES20.glAttachShader(colored_vertices_program_handle, fragmentShaderHandle);
            GLES20.glBindAttribLocation(colored_vertices_program_handle, 0, "a_Position");
            GLES20.glBindAttribLocation(colored_vertices_program_handle, 1, "a_Color");
            GLES20.glLinkProgram(colored_vertices_program_handle);

            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(colored_vertices_program_handle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            if (linkStatus[0] == 0)
            {
                GLES20.glDeleteProgram(colored_vertices_program_handle);
                colored_vertices_program_handle = 0;
            }
        }

        //Проверка на ошибку создания программы
        if (colored_vertices_program_handle == 0)
        {
            throw new RuntimeException("Error creating program.");
        }

        //создание матрицы, позиции, и цвета
        MVPmatrix_location = GLES20.glGetUniformLocation(colored_vertices_program_handle, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(colored_vertices_program_handle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(colored_vertices_program_handle, "a_Color");

        GLES20.glDisable(GLES20.GL_CULL_FACE);
        //Говорим программе что мы рендерим сцену
    }

    @Override
    // задаем ViewPort
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        //Первый вариант
        GLES20.glViewport(0,0,width,height);
        camera.setViewport(width,height);
    }

    boolean one_time=false;
    @Override
    // рисуем
    public void onDrawFrame(GL10 glUnused) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(colored_vertices_program_handle);
        GLES20.glUniformMatrix4fv(MVPmatrix_location, 1, false,camera.getViewProjectionMatrix().values, 0);
        drawColoredPoints(verticesReady, GLES20.GL_TRIANGLES, 0,30);
        drawColoredPoints(test_vertices, GLES20.GL_TRIANGLES, 0,3);

//        if(!one_time) {
//            Log.w("F",camera.getViewProjectionMatrix().debug());
//            one_time = true;
//        }
//        camera.translate(0.001f,0.0f);
    }

    //функция рисовки 3-ка
    private void drawColoredPoints(final FloatBuffer aTriangleBuffer, int render_type, int offset, int count)
    {
        // информация о позиции
        aTriangleBuffer.position(mPositionOffset);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, aTriangleBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // информация о цвете
        aTriangleBuffer.position(mColorOffset);
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, aTriangleBuffer);
        GLES20.glEnableVertexAttribArray(mColorHandle);

        //Вырисовываем 3-к
        GLES20.glDrawArrays(render_type, offset, count);
    }

}