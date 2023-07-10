package com.example.game.util;

import com.example.game.species.Circle;
import com.example.game.species.Rectangle;
import com.example.game.species.Shape;

public class Helper {
    public static Object[] sat(Shape a, Shape b) {
        Float minOverlap = null;
        Vec smallestAxis = null;
        Shape vertexObj = null;

        Vec[] axis = getAxis(a, b);
        int firstShapeAxis = getShapeAxis(a);

        Object[] proj1, proj2;
        float overlap;

        for (int i = 0; i < axis.length; i++) {
            proj1 = projectShapeOntoAxis(axis[i], a);
            proj2 = projectShapeOntoAxis(axis[i], b);
            overlap = Math.min((float)proj1[1], (float)proj2[1]) - Math.max((float)proj1[0], (float)proj2[0]);

            if (overlap < 0) return null;

            if (((float)proj1[1] > (float)proj2[1] && (float)proj1[0] < (float)proj2[0]) ||
                    ((float)proj1[1] < (float)proj2[1] && (float)proj1[0] > (float)proj2[0])) {

                float mins = Math.abs((float)proj1[0] - (float)proj2[0]);
                float maxs = Math.abs((float)proj1[1] - (float)proj2[1]);
                if (mins < maxs) {
                    overlap += mins;
                } else {
                    overlap += maxs;
                    axis[i] = axis[i].multy(-1);
                }
            }

            if (minOverlap == null || overlap < minOverlap ) {
                minOverlap = overlap;
                smallestAxis = axis[i];

                if (i < firstShapeAxis) {
                    vertexObj = b;
                    if ((float)proj1[1] > (float)proj2[1]) {
                        smallestAxis = axis[i].multy(-1);
                    }
                } else {
                    vertexObj = a;
                    if ((float)proj1[1] < (float)proj2[1]) {
                        smallestAxis = axis[i].multy(-1);
                    }
                }
            }
        }

        Vec contactVertex = (Vec)projectShapeOntoAxis(smallestAxis, vertexObj)[2];

        if(vertexObj == b) smallestAxis = smallestAxis.multy(-1);

        return new Object[] {minOverlap, smallestAxis, contactVertex};
    }

    public static Object[] projectShapeOntoAxis(Vec axis, Shape b) {
        setBallVerticesAlongAxis(b, axis);
        float min = Vec.dot(axis, b.getVertex()[0]);
        float max = min;
        Vec colVertex = b.getVertex()[0];

        for (int i = 0; i < b.getVertex().length; i++) {
            float p = Vec.dot(axis, b.getVertex()[i]);

            if (p < min) {
                min = p;

                colVertex = b.getVertex()[i];
            }

            if (p > max) {
                max = p;
            }
        }

        return new Object[] {min, max, colVertex};
    }

    public static Vec[] getAxis(Shape a, Shape b) {
        if (a instanceof Circle && b instanceof Circle) {
            if(b.getPosV().subtr(a.getPosV()).mag() > 0){
                return new Vec[] {
                        b.getPosV().subtr(a.getPosV()).unit()
                };
            } else {
                return new Vec[] {
                        new Vec((float)Math.random(), (float)Math.random()).unit()
                };
            }
        }

        if (a instanceof Circle) {
            if (b instanceof Rectangle) {
                return new Vec[] {
                        closestVertexToPoint(b, a.getPosV()).subtr(a.getPosV()).unit(),
                        b.getDirV().normal(),
                        b.getDirV()
                };
            } else {
                return new Vec[] {
                        closestVertexToPoint(b, a.getPosV()).subtr(a.getPosV()).unit(),
                        b.getDirV().normal()
                };
            }
        }

        if (b instanceof Circle) {
            if (a instanceof Rectangle) {
                return new Vec[] {
                        a.getDirV().normal(),
                        a.getDirV(),
                        closestVertexToPoint(a, b.getPosV()).subtr(b.getPosV()).unit()
                };
            } else {
                return new Vec[] {
                        a.getDirV().normal(),
                        closestVertexToPoint(a, b.getPosV()).subtr(b.getPosV()).unit()
                };
            }
        }

        if (a instanceof Rectangle && b instanceof Rectangle) {
            return new Vec[] {
                    a.getDirV().normal(),
                    a.getDirV(),
                    b.getDirV().normal(),
                    b.getDirV()
            };
        }

        else if (a instanceof Rectangle) {
            return new Vec[] {
                    a.getDirV().normal(),
                    a.getDirV(),
                    b.getDirV().normal()
            };
        }

        else if (b instanceof Rectangle) {
            return new Vec[] {
                    a.getDirV().normal(),
                    b.getDirV().normal(),
                    b.getDirV()
            };
        }

        else {
            return new Vec[] {
                    a.getDirV().normal(),
                    b.getDirV().normal()
            };
        }
    }

    public static Vec closestVertexToPoint(Shape b, Vec v) {
        Vec closestVertex = null;
        Float minDist = null;

        for (int i = 0; i < b.getVertex().length; i++) {
            if (minDist == null || v.subtr(b.getVertex()[i]).mag() < minDist) {
                closestVertex = b.getVertex()[i];
                minDist = v.subtr(b.getVertex()[i]).mag();
            }
        }
        return closestVertex;
    }

    public static int getShapeAxis(Shape obj) {
        if (obj instanceof Circle) return 1;
        else return 2;
    }

    public static void setBallVerticesAlongAxis(Shape obj, Vec axis) {
        if (obj instanceof Circle) {
            obj.setVertex( new Vec[2]);
            obj.getVertex()[0] = obj.getPosV().add(axis.unit().multy(-obj.getR()));
            obj.getVertex()[1] = obj.getPosV().add(axis.unit().multy(obj.getR()));
        }
    }

    public static boolean rectIntersect(float[] r1, float[] r2) {
        return r1[0] < r2[2] && r1[2] > r2[0] &&
                r1[1] < r2[3] && r1[3] > r2[1];
    }
}
