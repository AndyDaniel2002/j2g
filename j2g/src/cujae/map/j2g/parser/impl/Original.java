/* Generated By:JavaCC: Do not edit this line. Original.java */
package cujae.map.j2g.parser.impl;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

import cujae.map.j2g.parser.ThokenCollector;

@SuppressWarnings("all")
public class Original implements OriginalConstants {

  static final Set<String> nonInvertible = new HashSet<String>(
    Arrays.asList(new String []{
      "abs", "bessel", "ceiling", "divide", "floor", "integer",
      "modulus", "round", "signum", "step"
    }
  ));

  static boolean isNonInvertible(String fname) {
    return (fname != null) ? nonInvertible.contains(fname.toLowerCase()) : false;
  }

  final public void input(ThokenCollector c) throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case CALL:
    case IF:
    case MINUS:
    case LPARENT:
    case NAME:
    case SYMBOLIC:
    case NUMERIC:
    case LITERAL:
      oneLine(c);
      break;
    default:
      jj_la1[0] = jj_gen;
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SEMICOLON:
      jj_consume_token(SEMICOLON);
      break;
    default:
      jj_la1[1] = jj_gen;
      ;
    }
    jj_consume_token(0);
  }

  final public void oneLine(ThokenCollector c) throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IF:
      ifStatement(c);
      break;
    case CALL:
      callStatement(c);
      break;
    case MINUS:
    case LPARENT:
    case NAME:
    case SYMBOLIC:
    case NUMERIC:
    case LITERAL:
      statement(c, 0);
      break;
    default:
      jj_la1[2] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void statement(ThokenCollector c, int signum) throws ParseException {
    unary(c, signum);
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
      case MINUS:
      case MULTIPLY:
      case DIVIDE:
      case POW:
      case LOWER_THAN:
      case GREATER_THAN:
      case EQUAL:
      case LOWER_THAN_EQUAL:
      case GREATER_THAN_EQUAL:
      case NOT_EQUAL:
      case ASSIGN:
        ;
        break;
      default:
        jj_la1[3] = jj_gen;
        break label_1;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
        jj_consume_token(PLUS);
        break;
      case MINUS:
        jj_consume_token(MINUS);
        break;
      case MULTIPLY:
        jj_consume_token(MULTIPLY);
        break;
      case DIVIDE:
        jj_consume_token(DIVIDE);
        break;
      case POW:
        jj_consume_token(POW);
        break;
      case LOWER_THAN:
        jj_consume_token(LOWER_THAN);
        break;
      case GREATER_THAN:
        jj_consume_token(GREATER_THAN);
        break;
      case EQUAL:
        jj_consume_token(EQUAL);
        break;
      case LOWER_THAN_EQUAL:
        jj_consume_token(LOWER_THAN_EQUAL);
        break;
      case GREATER_THAN_EQUAL:
        jj_consume_token(GREATER_THAN_EQUAL);
        break;
      case NOT_EQUAL:
        jj_consume_token(NOT_EQUAL);
        break;
      case ASSIGN:
        jj_consume_token(ASSIGN);
        break;
      default:
        jj_la1[4] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      unary(c, signum);
    }
  }

  final public void unary(ThokenCollector c, int signum) throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case MINUS:
      jj_consume_token(MINUS);
      element(c, signum);
      break;
    case LPARENT:
    case NAME:
    case SYMBOLIC:
    case NUMERIC:
    case LITERAL:
      element(c, signum);
      break;
    default:
      jj_la1[5] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void element(ThokenCollector c, int signum) throws ParseException {
  Token t;
  boolean isFunction = false;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NUMERIC:
      jj_consume_token(NUMERIC);
      break;
    case LITERAL:
      jj_consume_token(LITERAL);
      break;
    case NAME:
    case SYMBOLIC:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NAME:
        t = jj_consume_token(NAME);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LPARENT:
        case LSQUARE:
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case LPARENT:
            jj_consume_token(LPARENT);
            if(isNonInvertible(t.image)) signum = -1;
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
            case MINUS:
            case LPARENT:
            case NAME:
            case SYMBOLIC:
            case NUMERIC:
            case LITERAL:
              parameterList(c, signum);
              break;
            default:
              jj_la1[6] = jj_gen;
              ;
            }
            isFunction = true;
            jj_consume_token(RPARENT);
            break;
          case LSQUARE:
            jj_consume_token(LSQUARE);
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
            case MINUS:
            case LPARENT:
            case NAME:
            case SYMBOLIC:
            case NUMERIC:
            case LITERAL:
              statement(c, signum);
              break;
            default:
              jj_la1[7] = jj_gen;
              ;
            }
            jj_consume_token(RSQUARE);
            break;
          default:
            jj_la1[8] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          break;
        default:
          jj_la1[9] = jj_gen;
          ;
        }
        if(!isFunction)
        {
          c.add(t.kind, t.beginLine, t.endLine, t.beginColumn, t.endColumn, t.image, !isFunction, signum, 0);
        }
        break;
      case SYMBOLIC:
        t = jj_consume_token(SYMBOLIC);
        if (jj_2_1(2)) {
          jj_consume_token(LSQUARE);
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case MINUS:
          case LPARENT:
          case NAME:
          case SYMBOLIC:
          case NUMERIC:
          case LITERAL:
            statement(c, signum);
            break;
          default:
            jj_la1[10] = jj_gen;
            ;
          }
          jj_consume_token(RSQUARE);
        } else {
          ;
        }
      c.add(t.kind, t.beginLine, t.endLine, t.beginColumn, t.endColumn, t.image, !isFunction, signum, 0);
        break;
      default:
        jj_la1[11] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
    case LPARENT:
      jj_consume_token(LPARENT);
      statement(c, signum);
      jj_consume_token(RPARENT);
      break;
    default:
      jj_la1[12] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void parameterList(ThokenCollector c, int signum) throws ParseException {
    statement(c, signum);
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[13] = jj_gen;
        break label_2;
      }
      jj_consume_token(COMMA);
      statement(c, signum);
    }
  }

  final public void ifStatement(ThokenCollector c) throws ParseException {
    jj_consume_token(IF);
    statement(c, -1);
    jj_consume_token(THEN);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case MINUS:
    case LPARENT:
    case NAME:
    case SYMBOLIC:
    case NUMERIC:
    case LITERAL:
      statement(c, 0);
      break;
    case CALL:
      callStatement(c);
      break;
    default:
      jj_la1[14] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ELSE:
      jj_consume_token(ELSE);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case MINUS:
      case LPARENT:
      case NAME:
      case SYMBOLIC:
      case NUMERIC:
      case LITERAL:
        statement(c, 0);
        break;
      case CALL:
        callStatement(c);
        break;
      default:
        jj_la1[15] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
    default:
      jj_la1[16] = jj_gen;
      ;
    }
  }

  final public void callStatement(ThokenCollector c) throws ParseException {
    jj_consume_token(CALL);
    jj_consume_token(NAME);
    jj_consume_token(LPARENT);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case MINUS:
    case LPARENT:
    case NAME:
    case SYMBOLIC:
    case NUMERIC:
    case LITERAL:
      parameterList(c, -1);
      break;
    default:
      jj_la1[17] = jj_gen;
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SEMICOLON:
      jj_consume_token(SEMICOLON);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case MINUS:
      case LPARENT:
      case NAME:
      case SYMBOLIC:
      case NUMERIC:
      case LITERAL:
        parameterList(c, 1);
        break;
      default:
        jj_la1[18] = jj_gen;
        ;
      }
      break;
    default:
      jj_la1[19] = jj_gen;
      ;
    }
    jj_consume_token(RPARENT);
  }

  private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  private boolean jj_3R_4() {
    if (jj_3R_5()) return true;
    return false;
  }

  private boolean jj_3R_5() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_6()) {
    jj_scanpos = xsp;
    if (jj_3R_7()) return true;
    }
    return false;
  }

  private boolean jj_3R_6() {
    if (jj_scan_token(MINUS)) return true;
    return false;
  }

  private boolean jj_3_1() {
    if (jj_scan_token(LSQUARE)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_3()) jj_scanpos = xsp;
    if (jj_scan_token(RSQUARE)) return true;
    return false;
  }

  private boolean jj_3R_12() {
    if (jj_scan_token(SYMBOLIC)) return true;
    return false;
  }

  private boolean jj_3R_11() {
    if (jj_scan_token(NAME)) return true;
    return false;
  }

  private boolean jj_3R_9() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_11()) {
    jj_scanpos = xsp;
    if (jj_3R_12()) return true;
    }
    return false;
  }

  private boolean jj_3R_8() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(35)) {
    jj_scanpos = xsp;
    if (jj_scan_token(36)) {
    jj_scanpos = xsp;
    if (jj_3R_9()) {
    jj_scanpos = xsp;
    if (jj_3R_10()) return true;
    }
    }
    }
    return false;
  }

  private boolean jj_3R_3() {
    if (jj_3R_4()) return true;
    return false;
  }

  private boolean jj_3R_10() {
    if (jj_scan_token(LPARENT)) return true;
    return false;
  }

  private boolean jj_3R_7() {
    if (jj_3R_8()) return true;
    return false;
  }

  /** Generated Token Manager. */
  public OriginalTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  private int jj_gen;
  final private int[] jj_la1 = new int[20];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x801060,0x8000000,0x801060,0x7ff800,0x7ff800,0x801000,0x801000,0x801000,0x2800000,0x2800000,0x801000,0x0,0x800000,0x10000000,0x801020,0x801020,0x100,0x801000,0x801000,0x8000000,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x1e,0x0,0x1e,0x0,0x0,0x1e,0x1e,0x1e,0x0,0x0,0x1e,0x6,0x1e,0x0,0x1e,0x1e,0x0,0x1e,0x1e,0x0,};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[1];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  /** Constructor with InputStream. */
  public Original(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Original(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new OriginalTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 20; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 20; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public Original(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new OriginalTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 20; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 20; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public Original(OriginalTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 20; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(OriginalTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 20; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[38];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 20; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 38; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

  private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 1; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
