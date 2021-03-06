/*
 * Copyright (c) 2015, Florent Hedin, Markus Meuwly, and the University of Basel
 * All rights reserved.
 *
 * The 3-clause BSD license is applied to this software.
 * see LICENSE.txt
 *
 */

package ch.unibas.charmmtools.internals;

/**
 *
 * @author hedin
 */
public class InternalCoordinates {

    //4 atoms composing the internal coordinates structure from charmm
    private Atom at1, at2, at3, at4;

    //Bond length between first two atoms
    //Bond length between last two atoms
    private double bndAB, bndCD;

    //Bond angle between first three atoms
    //Bond angle between last three atoms
    private double angABC, angBCD;

    //Torsion angle made by the four atoms (degree)
    private double diheABCD;

    //Flag indicating that this is an improper torsion
    private boolean isImproper;

    public InternalCoordinates(Atom _a1, Atom _a2, Atom _a3, Atom _a4, boolean _impr) {
        this.at1 = _a1;
        this.at2 = _a2;
        this.at3 = _a3;
        this.at4 = _a4;
        this.isImproper = _impr;

        if (!isImproper) {
            this.bndAB = Bond.calcLength(at1, at2);
            this.bndCD = Bond.calcLength(at3, at4);
            // false because we want the angles and dihedral in degrees
            this.angABC = Angle.calculateAngle(at1, at2, at3, false);
            this.angBCD = Angle.calculateAngle(at2, at3, at4, false);
            this.diheABCD = Dihedral.calculateDihe(at1, at2, at3, at4, false);
        } else {
            this.bndAB = Bond.calcLength(at1, at3);
            this.bndCD = Bond.calcLength(at3, at4);
            this.angABC = Angle.calculateAngle(at1, at3, at2, false);
            this.angBCD = Angle.calculateAngle(at2, at3, at4, false);
            this.diheABCD = Dihedral.calculateDihe(at1, at2, at3, at4, false);
        }

    }//ctor

    /**
     * @return the at1
     */
    public Atom getAt1() {
        return at1;
    }

    /**
     * @return the at2
     */
    public Atom getAt2() {
        return at2;
    }

    /**
     * @return the at3
     */
    public Atom getAt3() {
        return at3;
    }

    /**
     * @return the at4
     */
    public Atom getAt4() {
        return at4;
    }

    /**
     * @return the isImproper
     */
    public boolean isImproper() {
        return isImproper;
    }

    /**
     * @return the dihe
     */
    public double getDiheABCD() {
        return diheABCD;
    }

    /**
     * @return the bndAB
     */
    public double getBndAB() {
        return bndAB;
    }

    /**
     * @return the bndCD
     */
    public double getBndCD() {
        return bndCD;
    }

    /**
     * @return the angABC
     */
    public double getAngABC() {
        return angABC;
    }

    /**
     * @return the angBCD
     */
    public double getAngBCD() {
        return angBCD;
    }

}
