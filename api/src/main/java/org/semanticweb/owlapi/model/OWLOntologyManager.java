/*
 * This file is part of the OWL API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2014, The University of Manchester
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2014, The University of Manchester
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.semanticweb.owlapi.model;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.io.OWLOntologyDocumentTarget;
import org.semanticweb.owlapi.io.OWLParser;

/** An {@code OWLOntologyManager} manages a set of ontologies. It is the main
 * point for creating, loading and accessing ontologies. <br>
 * An {@code OWLOntologyManager} also manages the mapping betweem an ontology and
 * its ontology document.
 * 
 * @author Matthew Horridge, The University Of Manchester, Bio-Health
 *         Informatics Group Date: 24-Oct-2006 */
public interface OWLOntologyManager extends OWLOntologySetProvider {
    /** Gets a data factory which can be used to create OWL API objects such as
     * classes, properties, individuals, axioms etc.
     * 
     * @return A reference to a data factory for creating OWL API objects. */
    @Nonnull
    OWLDataFactory getOWLDataFactory();

    @Override
    @Nonnull
    Set<OWLOntology> getOntologies();

    /** Gets the ontologies that are managed by this manager that contain the
     * specified axiom.
     * 
     * @param axiom
     *            The axioms
     * @return The set of ontologies such that for each ontology, O the
     *         specified axiom is contained in O. */
    @Nonnull
    Set<OWLOntology> getOntologies(@Nonnull OWLAxiom axiom);

    /** Gets the versions (if any) of the ontology that have the specified IRI
     * 
     * @param ontology
     *            The ontology IRI
     * @return The set of ontologies that have the specified ontology IRI. */
    @Nonnull
    Set<OWLOntology> getVersions(@Nonnull IRI ontology);

    /** @param ontology
     *            ontology to check
     * @return true if the ontology is contained */
    boolean contains(@Nonnull OWLOntology ontology);

    /** Determines if there is an ontology with the specified IRI, and no version
     * IRI, that is managed by this manager
     * 
     * @param ontologyIRI
     *            The IRI of the ontology to test for (the version IRI is
     *            assumed to be {@code null})
     * @return {@code true} if there is an ontology with the specified IRI, and
     *         no version IRI, that is managed by this manager, otherwise
     *         {@code false}. */
    boolean contains(@Nonnull IRI ontologyIRI);

    /** Determines if there is an ontology with the specified id that is managed
     * by this manager
     * 
     * @param id
     *            The id of the ontology to test for
     * @return {@code true} if there is an ontology with the specified id that
     *         is managed by this manager, otherwise {@code false}. */
    boolean contains(@Nonnull OWLOntologyID id);

    /** Determines if there is an ontology with the specified version IRI, that
     * is managed by this manager.
     * 
     * @param ontologyVersionIRI
     *            The version IRI of the ontology to test for (the ontology IRI
     *            may be anything)
     * @return {@code true} if there is an ontology with the specified version
     *         IRI, that is managed by this manager, otherwise {@code false}. */
    boolean containsVersion(@Nonnull IRI ontologyVersionIRI);

    /** Gets a set of OWLOntologyIDs representing ontologies that are managed by
     * this manager.
     * 
     * @param ontologyVersionIRI
     *            The version IRI to match against all of the known ontologies.
     * @return A set of OWLOntologyIDs where the version matches the given
     *         version or the empty set if none match. */
    @Nonnull
    Set<OWLOntologyID> getOntologyIDsByVersion(@Nonnull IRI ontologyVersionIRI);

    /** Gets a previously loaded/created ontology that has the specified ontology
     * IRI and no version IRI.
     * 
     * @param ontologyIRI
     *            The IRI of the ontology to be retrieved.
     * @return The ontology that has the specified IRI and no version IRI, or
     *         {@code null} if this manager does not manage an ontology with the
     *         specified IRI and no version IRI. */
    @Nonnull
    OWLOntology getOntology(@Nonnull IRI ontologyIRI);

    /** Gets a previously loaded/created ontology that has the specified ontology
     * ID
     * 
     * @param ontologyID
     *            The ID of the ontology to retrieve
     * @return The ontology that has the specified ID, or {@code null} if this
     *         manager does not manage an ontology with the specified ontology
     *         ID. */
    @Nullable
    OWLOntology getOntology(@Nonnull OWLOntologyID ontologyID);

    /** Given an imports declaration, obtains the ontology that this import has
     * been resolved to.
     * 
     * @param declaration
     *            The declaration that points to the imported ontology.
     * @return The ontology that the imports declaration resolves to, or
     *         {@code null} if the imports declaration could not be resolved to
     *         an ontology, because the ontology was not loaded or has been
     *         removed from this manager */
    @Nullable
    OWLOntology getImportedOntology(@Nonnull OWLImportsDeclaration declaration);

    /** Gets the set of <em>loaded</em> ontologies that the specified ontology is
     * related to via the directlyImports relation as defined in Section 3.4 of
     * the OWL 2 Structural specification
     * 
     * @param ontology
     *            The ontology whose direct imports are to be retrieved.
     * @return The set of <em>loaded</em> ontologies that the specified ontology
     *         is related to via the directlyImports relation. If the ontology
     *         is not managed by this manager then the empty set will be
     *         returned. */
    @Nonnull
    Set<OWLOntology> getDirectImports(@Nonnull OWLOntology ontology);

    /** Gets the set of ontologies that are in the transitive closure of the
     * directly imports relation.
     * 
     * @param ontology
     *            The ontology whose imports are to be retrieved.
     * @return A set of {@code OWLOntology}ies that are in the transitive
     *         closure of the directly imports relation of this ontology. If,
     *         for what ever reason, an imported ontology could not be loaded,
     *         then it will not be contained in the returned set of ontologies.
     *         If the ontology is not managed by this manager then the empty set
     *         will be returned. */
    @Nonnull
    Set<OWLOntology> getImports(@Nonnull OWLOntology ontology);

    /** Gets the imports closure for the specified ontology.
     * 
     * @param ontology
     *            The ontology whose imports closure is to be retrieved.
     * @return A {@code Set} of ontologies that contains the imports closure for
     *         the specified ontology. This set will also include the specified
     *         ontology. Example: if A imports B and B imports C, then calling
     *         this method with A will return the set consisting of A, B and C.
     *         If, for what ever reason, an imported ontology could not be
     *         loaded, then it will not be contained in the returned set of
     *         ontologies. If the ontology is not managed by this manager then
     *         the empty set will be returned. */
    @Nonnull
    Set<OWLOntology> getImportsClosure(@Nonnull OWLOntology ontology);

    /** Gets the topologically ordered imports closure.
     * 
     * @param ontology
     *            The ontology whose imports closure is to be determined.
     * @return A list that represents a topological ordering of the imports
     *         closure. The first element in the list will be the specified
     *         ontology. If the ontology is not managed by this manager then an
     *         empty list will be returned. */
    @Nonnull
    List<OWLOntology> getSortedImportsClosure(@Nonnull OWLOntology ontology);

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // //
    // // Ontology change
    // //
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /** Applies a list of changes to some or all of the ontologies that are
     * managed by this manager. The changes will be applied to the appropriate
     * ontologies.
     * 
     * @param changes
     *            The changes to be applied.
     * @return The changes that were actually applied.
     * @throws OWLOntologyChangeException
     *             If one or more of the changes could not be applied. See
     *             subclasses of ontology change exception for more specific
     *             details.
     * @throws OWLOntologyRenameException
     *             If one or more of the changes is an instance of
     *             {@link org.semanticweb.owlapi.model.SetOntologyID} where the
     *             new {@link org.semanticweb.owlapi.model.OWLOntologyID}
     *             already belongs to an ontology managed by this manager. */
    @Nonnull
    List<OWLOntologyChange<?>> applyChanges(
            @Nonnull List<? extends OWLOntologyChange<?>> changes)
            throws OWLOntologyRenameException;

    /** A convenience method that adds a set of axioms to an ontology. The
     * appropriate AddAxiom change objects are automatically generated.
     * 
     * @param ont
     *            The ontology to which the axioms should be added.
     * @param axioms
     *            The axioms to be added.
     * @return A list of ontology changes that represent the changes which took
     *         place in order to add the axioms.
     * @throws OWLOntologyChangeException
     *             if there was a problem adding the axioms */
    @Nonnull
    List<OWLOntologyChange<?>> addAxioms(@Nonnull OWLOntology ont,
            @Nonnull Set<? extends OWLAxiom> axioms);

    /** A convenience method that adds a single axiom to an ontology. The
     * appropriate AddAxiom change object is automatically generated.
     * 
     * @param ont
     *            The ontology to add the axiom to.
     * @param axiom
     *            The axiom to be added
     * @return A list of ontology changes that represent the changes that
     *         actually took place.
     * @throws OWLOntologyChangeException
     *             if there was a problem adding the axiom */
    @Nonnull
    List<OWLOntologyChange<?>> addAxiom(@Nonnull OWLOntology ont,
            @Nonnull OWLAxiom axiom);

    /** A convenience method that removes a single axiom from an ontology. The
     * appropriate RemoveAxiom change object is automatically generated.
     * 
     * @param ont
     *            The ontology to remove the axiom from.
     * @param axiom
     *            The axiom to be removed
     * @return A list of ontology changes that represent the changes that
     *         actually took place.
     * @throws OWLOntologyChangeException
     *             if there was a problem removing the axiom */
    @Nonnull
    List<OWLOntologyChange<?>> removeAxiom(@Nonnull OWLOntology ont,
            @Nonnull OWLAxiom axiom);

    /** A convenience method that removes a set of axioms from an ontology. The
     * appropriate RemoveAxiom change objects are automatically generated.
     * 
     * @param ont
     *            The ontology from which the axioms should be removed.
     * @param axioms
     *            The axioms to be removed.
     * @return A list of ontology changes that represent the changes which took
     *         place in order to remove the axioms.
     * @throws OWLOntologyChangeException
     *             if there was a problem removing the axioms */
    @Nonnull
    List<OWLOntologyChange<?>> removeAxioms(@Nonnull OWLOntology ont,
            @Nonnull Set<? extends OWLAxiom> axioms);

    /** A convenience method that applies just one change to an ontology that is
     * managed by this manager.
     * 
     * @param change
     *            The change to be applied
     * @return The changes that resulted of the applied ontology change.
     * @throws OWLOntologyChangeException
     *             If the change could not be applied. See subclasses of
     *             ontology change exception for more specific details.
     * @throws OWLOntologyRenameException
     *             If one or more of the changes is an instance of
     *             {@link org.semanticweb.owlapi.model.SetOntologyID} where the
     *             new {@link org.semanticweb.owlapi.model.OWLOntologyID}
     *             already belongs to an ontology managed by this manager. */
    @Nonnull
    List<OWLOntologyChange<?>>
            applyChange(@Nonnull OWLOntologyChange<?> change)
                    throws OWLOntologyRenameException;

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // //
    // // Ontology creation
    // //
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /** Creates a new (empty) ontology that does not have an ontology IRI (and
     * therefore does not have a version IRI). A document IRI will automatically
     * be generated.
     * 
     * @return The newly created ontology
     * @throws OWLOntologyCreationException
     *             if there was a problem creating the ontology */
    @Nonnull
    OWLOntology createOntology() throws OWLOntologyCreationException;

    /** Creates a new ontology that is initialised to contain specific axioms.
     * The ontology will not have an IRI. The document IRI of the created
     * ontology will be auto-generated.
     * 
     * @param axioms
     *            The axioms that should be copied into the new ontology
     * @return An ontology without an IRI that contains all of the specified
     *         axioms
     * @throws OWLOntologyCreationException
     *             if there was a problem creating the new ontology.
     * @throws OWLOntologyChangeException
     *             if there was a problem copying the axioms. */
    @Nonnull
    OWLOntology createOntology(@Nonnull Set<OWLAxiom> axioms)
            throws OWLOntologyCreationException;

    /** Creates a new ontology that has the specified ontology IRI and is
     * initialised to contain specific axioms.
     * 
     * @param ontologyIRI
     *            The IRI of the new ontology. <br>
     *            The ontology document IRI of the created ontology will be set
     *            to the value returned by any installed
     *            {@link org.semanticweb.owlapi.model.OWLOntologyIRIMapper}s. If
     *            no mappers are installed or the ontology IRI was not mapped to
     *            a document IRI by any of the installed mappers, then the
     *            ontology document IRI will be set to the value of
     *            {@code ontologyIRI}.
     * @param axioms
     *            The axioms that should be copied into the new ontology
     * @return An ontology that has the specified IRI and contains all of the
     *         specified axioms
     * @throws OWLOntologyCreationException
     *             if there was a problem creating the new ontology, if the new
     *             ontology already exists in this manager.
     * @throws OWLOntologyChangeException
     *             if there was a problem copying the axioms.
     * @throws OWLOntologyAlreadyExistsException
     *             if the manager already contains an ontology with the
     *             specified {@code ontologyIRI}.
     * @throws OWLOntologyDocumentAlreadyExistsException
     *             if the specified {@code ontologyIRI} is mapped to a ontology
     *             document IRI for which there already exists a mapping in this
     *             manager. */
    @Nonnull
    OWLOntology createOntology(@Nonnull Set<OWLAxiom> axioms,
            @Nonnull IRI ontologyIRI) throws OWLOntologyCreationException;

    /** Creates a new (empty) ontology that has the specified ontology IRI (and
     * no version IRI). <br>
     * The ontology document IRI of the created ontology will be set to the
     * value returned by any installed
     * {@link org.semanticweb.owlapi.model.OWLOntologyIRIMapper}s. If no mappers
     * are installed or the ontology IRI was not mapped to a document IRI by any
     * of the installed mappers, then the ontology document IRI will be set to
     * the value of {@code ontologyIRI}.
     * 
     * @param ontologyIRI
     *            The IRI of the ontology to be created. The ontology IRI will
     *            be mapped to a document IRI in order to determine the type of
     *            ontology factory that will be used to create the ontology. If
     *            this mapping is {@code null} then a default (in memory)
     *            implementation of the ontology will most likely be created.
     * @return The newly created ontology, or if an ontology with the specified
     *         IRI already exists then this existing ontology will be returned.
     * @throws OWLOntologyCreationException
     *             If the ontology could not be created.
     * @throws OWLOntologyAlreadyExistsException
     *             if the manager already contains an ontology with the
     *             specified {@code ontologyIRI} (and no version IRI).
     * @throws OWLOntologyDocumentAlreadyExistsException
     *             if the specified {@code ontologyIRI} is mapped to a ontology
     *             document IRI for which there already exists a mapping in this
     *             manager. */
    @Nonnull
    OWLOntology createOntology(@Nonnull IRI ontologyIRI)
            throws OWLOntologyCreationException;

    /** Creates a new (empty) ontology that has the specified ontology ID.
     * 
     * @param ontologyID
     *            The ID of the ontology to be created. <br>
     *            The ontology document IRI of the created ontology will be set
     *            to the value returned by any installed
     *            {@link org.semanticweb.owlapi.model.OWLOntologyIRIMapper}s. If
     *            no mappers are installed or the ontology IRI was not mapped to
     *            a document IRI by any of the installed mappers, then the
     *            ontology document IRI will be set to the value of
     *            {@code ontologyIRI}.
     * @return The newly created ontology, or if an ontology with the specified
     *         IRI already exists then this existing ontology will be returned.
     * @throws OWLOntologyCreationException
     *             If the ontology could not be created.
     * @throws OWLOntologyAlreadyExistsException
     *             if the manager already contains an ontology with the
     *             specified {@code ontologyID} (and no version IRI).
     * @throws OWLOntologyDocumentAlreadyExistsException
     *             if the specified {@code ontologyID} is mapped to a ontology
     *             document IRI for which there already exists a mapping in this
     *             manager. */
    @Nonnull
    OWLOntology createOntology(@Nonnull OWLOntologyID ontologyID)
            throws OWLOntologyCreationException;

    /** Creates a new ontology that has the specified ontology IRI and is
     * initialised to contain the axioms that are contained in the specified
     * ontologies. Note that the specified ontologies need not be managed by
     * this manager. <br>
     * The ontology document IRI of the created ontology will be set to the
     * value returned by any installed
     * {@link org.semanticweb.owlapi.model.OWLOntologyIRIMapper}s. If no mappers
     * are installed or the ontology IRI was not mapped to a document IRI by any
     * of the installed mappers, then the ontology document IRI will be set to
     * the value of {@code ontologyIRI}.
     * 
     * @param ontologyIRI
     *            The IRI of the new ontology.
     * @param ontologies
     *            The ontologies whose axioms should be copied into the new
     *            ontology
     * @param copyLogicalAxiomsOnly
     *            If set to {@code true} only logical axioms are copied into the
     *            new ontology. If set to {@code false} then all axioms
     *            (including annotation axioms) are copied into the new
     *            ontology.
     * @return An ontology that has the specified IRI and contains all of the
     *         axioms that are contained in the specified ontologies possibly
     *         minus all non-logical axioms
     * @throws OWLOntologyCreationException
     *             if there was a problem creating the new ontology, if the new
     *             ontology already exists in this manager.
     * @throws OWLOntologyAlreadyExistsException
     *             if the manager already contains an ontology with the
     *             specified {@code ontologyIRI} (and no ontology version IRI).
     * @throws OWLOntologyDocumentAlreadyExistsException
     *             if the specified {@code ontologyIRI} is mapped to a ontology
     *             document IRI for which there already exists a mapping in this
     *             manager. */
    @Nonnull
    OWLOntology
            createOntology(@Nonnull IRI ontologyIRI,
                    @Nonnull Set<OWLOntology> ontologies,
                    boolean copyLogicalAxiomsOnly)
                    throws OWLOntologyCreationException;

    /** Creates a new ontology that has the specified ontology IRI and is
     * initialised to contain the axioms that are contained in the specified
     * ontologies. Note that the specified ontologies need not be managed by
     * this manager. <br>
     * The ontology document IRI of the created ontology will be set to the
     * value returned by any installed
     * {@link org.semanticweb.owlapi.model.OWLOntologyIRIMapper}s. If no mappers
     * are installed or the ontology IRI was not mapped to a document IRI by any
     * of the installed mappers, then the ontology document IRI will be set to
     * the value of {@code ontologyIRI}.
     * 
     * @param ontologyIRI
     *            The IRI of the new ontology.
     * @param ontologies
     *            The ontologies whose axioms should be copied into the new
     *            ontology
     * @return An ontology that has the specified IRI and contains all of the
     *         axioms that are contained in the specified ontologies
     * @throws OWLOntologyCreationException
     *             if there was a problem creating the new ontology, if the new
     *             ontology already exists in this manager.
     * @throws OWLOntologyAlreadyExistsException
     *             if the manager already contains an ontology with the
     *             specified {@code ontologyIRI} (and no version IRI).
     * @throws OWLOntologyDocumentAlreadyExistsException
     *             if the specified {@code ontologyIRI} is mapped to a ontology
     *             document IRI for which there already exists a mapping in this
     *             manager. */
    @Nonnull
    OWLOntology createOntology(@Nonnull IRI ontologyIRI,
            @Nonnull Set<OWLOntology> ontologies)
            throws OWLOntologyCreationException;

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // //
    // // Loading
    // //
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /** Loads an ontology that is assumed to have the specified
     * {@code ontologyIRI} as its IRI or version IRI. <br>
     * The ontology IRI will be mapped to an ontology document IRI. The mapping
     * will be determined using one of the loaded {@link OWLOntologyIRIMapper}
     * objects. By default, if no custom {@code OWLOntologyIRIMapper}s have been
     * registered using the {@link #addIRIMapper(OWLOntologyIRIMapper)} method,
     * or no mapping can be found, the ontology document IRI is taken to be the
     * specified ontology IRI.
     * 
     * @param ontologyIRI
     *            The IRI that identifies the ontology. It is expected that the
     *            ontology will also have this IRI (although the OWL API will
     *            tolerated situations where this is not the case).
     * @return The {@code OWLOntology} representation of the ontology that was
     *         loaded.
     * @throws OWLOntologyCreationException
     *             If there was a problem in creating and loading the ontology.
     * @throws org.semanticweb.owlapi.io.UnparsableOntologyException
     *             if the ontology was being parsed from a document and the
     *             document contained syntax errors.
     * @throws UnloadableImportException
     *             if the ontology imports ontologies and one of the imports
     *             could not be loaded for what ever reason. If the
     *             {@link MissingImportHandlingStrategy} is set to
     *             {@link MissingImportHandlingStrategy#SILENT} then this
     *             exception will not be thrown. The
     *             {@code UnloadableImportException} contains information about
     *             the import declaration that triggered the import and the
     *             cause of this exception is an
     *             {@code OWLOntologyCreationException} which contains
     *             information about why the import could not be loaded.
     * @throws org.semanticweb.owlapi.io.OWLOntologyCreationIOException
     *             if there was an {@code IOException} when trying to load the
     *             ontology.
     * @throws OWLOntologyAlreadyExistsException
     *             if the manager already contains an ontology with the
     *             specified {@code ontologyIRI} (where the ontology doesn't
     *             have a version IRI).
     * @throws OWLOntologyDocumentAlreadyExistsException
     *             if the specified {@code ontologyIRI} is mapped to a ontology
     *             document IRI for which there already exists a mapping in this
     *             manager. */
    @Nonnull
    OWLOntology loadOntology(@Nonnull IRI ontologyIRI)
            throws OWLOntologyCreationException;

    /** Loads an ontology from an ontology document specified by an IRI. In
     * contrast the the {@link #loadOntology(IRI)} method, <i>no mapping</i> is
     * performed on the specified IRI.
     * 
     * @param documentIRI
     *            The ontology document IRI where the ontology will be loaded
     *            from.
     * @return The ontology that was loaded.
     * @throws OWLOntologyCreationException
     *             If there was a problem in creating and loading the ontology.
     * @throws org.semanticweb.owlapi.io.UnparsableOntologyException
     *             if the ontology was being parsed from a document and the
     *             document contained syntax errors.
     * @throws UnloadableImportException
     *             if the ontology imports ontologies and one of the imports
     *             could not be loaded for what ever reason. If the
     *             {@link MissingImportHandlingStrategy} is set to
     *             {@link MissingImportHandlingStrategy#SILENT} then this
     *             exception will not be thrown. The
     *             {@code UnloadableImportException} contains information about
     *             the import declaration that triggered the import and the
     *             cause of this exception is an
     *             {@code OWLOntologyCreationException} which contains
     *             information about why the import could not be loaded.
     * @throws org.semanticweb.owlapi.io.OWLOntologyCreationIOException
     *             if there was an {@code IOException} when trying to load the
     *             ontology.
     * @throws OWLOntologyDocumentAlreadyExistsException
     *             if the specified {@code documentIRI} is already the document
     *             IRI for a loaded ontology.
     * @throws OWLOntologyAlreadyExistsException
     *             if the manager already contains an ontology whose ontology
     *             IRI and version IRI is the same as the ontology IRI and
     *             version IRI of the ontology contained in the document pointed
     *             to by {@code documentIRI}. */
    @Nonnull
    OWLOntology loadOntologyFromOntologyDocument(@Nonnull IRI documentIRI)
            throws OWLOntologyCreationException;

    /** Loads an ontology from an ontology document contained in a local file.
     * The loaded ontology will be assigned a document IRI that corresponds to
     * the file IRI.
     * 
     * @param file
     *            The file that contains a representation of an ontology
     * @return The ontology that was parsed from the file.
     * @throws OWLOntologyCreationException
     *             If there was a problem in creating and loading the ontology.
     * @throws org.semanticweb.owlapi.io.UnparsableOntologyException
     *             if the ontology could not be parsed.
     * @throws UnloadableImportException
     *             if the ontology imports ontologies and one of the imports
     *             could not be loaded for what ever reason. If the
     *             {@link MissingImportHandlingStrategy} is set to
     *             {@link MissingImportHandlingStrategy#SILENT} then this
     *             exception will not be thrown. The
     *             {@code UnloadableImportException} contains information about
     *             the import declaration that triggered the import and the
     *             cause of this exception is an
     *             {@code OWLOntologyCreationException} which contains
     *             information about why the import could not be loaded.
     * @throws org.semanticweb.owlapi.io.OWLOntologyCreationIOException
     *             if there was an {@code IOException} when trying to load the
     *             ontology.
     * @throws OWLOntologyDocumentAlreadyExistsException
     *             if the IRI of the specified file is already the document IRI
     *             for a loaded ontology.
     * @throws OWLOntologyAlreadyExistsException
     *             if the manager already contains an ontology whose ontology
     *             IRI and version IRI is the same as the ontology IRI and
     *             version IRI of the ontology contained in the document pointed
     *             to by {@code documentIRI}. */
    @Nonnull
    OWLOntology loadOntologyFromOntologyDocument(@Nonnull File file)
            throws OWLOntologyCreationException;

    /** Loads an ontology from an ontology document obtained from an input
     * stream. The loaded ontology will be assigned an auto-generated document
     * IRI with "inputstream" as its scheme.
     * 
     * @param inputStream
     *            The input stream that can be used to obtain a representation
     *            of an ontology
     * @return The ontology that was parsed from the input stream.
     * @throws OWLOntologyCreationException
     *             If there was a problem in creating and loading the ontology.
     * @throws org.semanticweb.owlapi.io.UnparsableOntologyException
     *             if the ontology could not be parsed.
     * @throws UnloadableImportException
     *             if the ontology imports ontologies and one of the imports
     *             could not be loaded for what ever reason. If the
     *             {@link MissingImportHandlingStrategy} is set to
     *             {@link MissingImportHandlingStrategy#SILENT} then this
     *             exception will not be thrown. The
     *             {@code UnloadableImportException} contains information about
     *             the import declaration that triggered the import and the
     *             cause of this exception is an
     *             {@code OWLOntologyCreationException} which contains
     *             information about why the import could not be loaded.
     * @throws org.semanticweb.owlapi.io.OWLOntologyCreationIOException
     *             if there was an {@code IOException} when trying to load the
     *             ontology.
     * @throws OWLOntologyAlreadyExistsException
     *             if the manager already contains an ontology whose ontology
     *             IRI and version IRI is the same as the ontology IRI and
     *             version IRI of the ontology obtained from parsing the content
     *             of the input stream. */
    @Nonnull
    OWLOntology loadOntologyFromOntologyDocument(
            @Nonnull InputStream inputStream)
            throws OWLOntologyCreationException;

    /** A convenience method that load an ontology from an input source.
     * 
     * @param documentSource
     *            The input source that describes where the ontology should be
     *            loaded from.
     * @return The ontology that was loaded.
     * @throws OWLOntologyCreationException
     *             If there was a problem in creating and loading the ontology.
     * @throws org.semanticweb.owlapi.io.UnparsableOntologyException
     *             if the ontology was being parsed from a document and the
     *             document contained syntax errors.
     * @throws UnloadableImportException
     *             if the ontology imports ontologies and one of the imports
     *             could not be loaded for what ever reason. If the
     *             {@link MissingImportHandlingStrategy} is set to
     *             {@link MissingImportHandlingStrategy#SILENT} then this
     *             exception will not be thrown. The
     *             {@code UnloadableImportException} contains information about
     *             the import declaration that triggered the import and the
     *             cause of this exception is an
     *             {@code OWLOntologyCreationException} which contains
     *             information about why the import could not be loaded.
     * @throws org.semanticweb.owlapi.io.OWLOntologyCreationIOException
     *             if there was an {@code IOException} when trying to load the
     *             ontology.
     * @throws OWLOntologyDocumentAlreadyExistsException
     *             if the document IRI of the input source is already the
     *             document IRI for a loaded ontology.
     * @throws OWLOntologyAlreadyExistsException
     *             if the manager already contains an ontology whose ontology
     *             IRI and version IRI is the same as the ontology IRI and
     *             version IRI of the ontology contained in the document
     *             represented by the input source. */
    @Nonnull
    OWLOntology loadOntologyFromOntologyDocument(
            @Nonnull OWLOntologyDocumentSource documentSource)
            throws OWLOntologyCreationException;

    /** A convenience method that load an ontology from an input source with
     * specified configuration.
     * 
     * @param documentSource
     *            The input source that describes where the ontology should be
     *            loaded from.
     * @param config
     *            the configuration to use
     * @return The ontology that was loaded.
     * @throws OWLOntologyCreationException
     *             If there was a problem in creating and loading the ontology.
     * @throws org.semanticweb.owlapi.io.UnparsableOntologyException
     *             if the ontology was being parsed from a document and the
     *             document contained syntax errors.
     * @throws UnloadableImportException
     *             if the ontology imports ontologies and one of the imports
     *             could not be loaded for what ever reason. If the
     *             {@link MissingImportHandlingStrategy} is set to
     *             {@link MissingImportHandlingStrategy#SILENT} then this
     *             exception will not be thrown. The
     *             {@code UnloadableImportException} contains information about
     *             the import declaration that triggered the import and the
     *             cause of this exception is an
     *             {@code OWLOntologyCreationException} which contains
     *             information about why the import could not be loaded.
     * @throws org.semanticweb.owlapi.io.OWLOntologyCreationIOException
     *             if there was an {@code IOException} when trying to load the
     *             ontology.
     * @throws OWLOntologyDocumentAlreadyExistsException
     *             if the document IRI of the input source is already the
     *             document IRI for a loaded ontology.
     * @throws OWLOntologyAlreadyExistsException
     *             if the manager already contains an ontology whose ontology
     *             IRI and version IRI is the same as the ontology IRI and
     *             version IRI of the ontology contained in the document
     *             represented by the input source. */
    @Nonnull
    OWLOntology loadOntologyFromOntologyDocument(
            @Nonnull OWLOntologyDocumentSource documentSource,
            @Nonnull OWLOntologyLoaderConfiguration config)
            throws OWLOntologyCreationException;

    /** Attempts to remove an ontology. The ontology which is identified by the
     * specified IRI is removed regardless of whether it is referenced by other
     * ontologies via imports statements.
     * 
     * @param ontology
     *            The ontology to be removed. If this manager does not manage
     *            the ontology then nothing happens. */
    void removeOntology(@Nonnull OWLOntology ontology);

    /** Attempts to remove an ontology. The ontology which is identified by the
     * specified IRI is removed regardless of whether it is referenced by other
     * ontologies via imports statements.
     * 
     * @param ontologyID
     *            The ontology to be removed. If this manager does not manage
     *            the ontology then nothing happens. */
    void removeOntology(@Nonnull OWLOntologyID ontologyID);

    /** Gets the document IRI for a given ontology. This will either be the
     * document IRI from where the ontology was obtained from during loading, or
     * the document IRI which was specified (via a mapper) when the (empty)
     * ontology was created. Note that this may not correspond to the first
     * document IRI found in the list of mappings from ontology IRI to document
     * IRI. The reason for this is that it might not have been possible to load
     * the ontology from the first document IRI found in the mapping table.
     * 
     * @param ontology
     *            The ontology whose document IRI is to be obtained.
     * @return The document IRI of the ontology or {@code null}.
     * @throws UnknownOWLOntologyException
     *             If the specified ontology is not managed by this manager. */
    @Nonnull
    IRI getOntologyDocumentIRI(@Nonnull OWLOntology ontology);

    /** Overrides the current document IRI for a given ontology. This method does
     * not alter the IRI mappers which are installed, but alters the actual
     * document IRI of an ontology that has already been loaded.
     * 
     * @param ontology
     *            The ontology that has already been loaded.
     * @param documentIRI
     *            The new ontology document IRI
     * @throws UnknownOWLOntologyException
     *             If the specified ontology is not managed by this manager. */
    void setOntologyDocumentIRI(@Nonnull OWLOntology ontology,
            @Nonnull IRI documentIRI) throws UnknownOWLOntologyException;

    /** Gets the ontology format for the specified ontology.
     * 
     * @param ontology
     *            The ontology whose format it to be obtained.
     * @return The format of the ontology.
     * @throws UnknownOWLOntologyException
     *             If the specified ontology is not managed by this manager. */
    @Nonnull
    OWLOntologyFormat getOntologyFormat(@Nonnull OWLOntology ontology)
            throws UnknownOWLOntologyException;

    /** Sets the format for the specified ontology.
     * 
     * @param ontology
     *            The ontology whose format is to be set.
     * @param ontologyFormat
     *            The format for the specified ontology.
     * @throws UnknownOWLOntologyException
     *             If the specified ontology is not managed by this manager. */
    void setOntologyFormat(@Nonnull OWLOntology ontology,
            @Nonnull OWLOntologyFormat ontologyFormat);

    /** Saves the specified ontology. The ontology will be saved to the location
     * that it was loaded from, or if it was created programmatically, it will
     * be saved to the location specified by an ontology IRI mapper at creation
     * time. The ontology will be saved in the same format which it was loaded
     * from, or the default ontology format if the ontology was created
     * programmatically.
     * 
     * @param ontology
     *            The ontology to be saved.
     * @throws OWLOntologyStorageException
     *             An exception will be thrown if there is a problem with saving
     *             the ontology, or the ontology can't be saved in the format it
     *             was loaded from.
     * @throws UnknownOWLOntologyException
     *             if this manager does not manage the specified ontology */
    void saveOntology(@Nonnull OWLOntology ontology)
            throws OWLOntologyStorageException;

    /** Saves the specified ontology, using the specified document IRI to
     * determine where/how the ontology should be saved.
     * 
     * @param ontology
     *            The ontology to be saved.
     * @param documentIRI
     *            The document IRI where the ontology should be saved to
     * @throws OWLOntologyStorageException
     *             If the ontology cannot be saved
     * @throws UnknownOWLOntologyException
     *             if the specified ontology is not managed by this manager. */
    void saveOntology(@Nonnull OWLOntology ontology, @Nonnull IRI documentIRI)
            throws OWLOntologyStorageException;

    /** Saves the specified ontology, to the specified output stream
     * 
     * @param ontology
     *            The ontology to be saved.
     * @param outputStream
     *            The output stream where the ontology will be saved to
     * @throws OWLOntologyStorageException
     *             If there was a problem saving this ontology to the specified
     *             output stream
     * @throws UnknownOWLOntologyException
     *             if this manager does not manage the specified ontology. */
    void saveOntology(@Nonnull OWLOntology ontology,
            @Nonnull OutputStream outputStream)
            throws OWLOntologyStorageException;

    /** Saves the specified ontology in the specified ontology format to its
     * document URI.
     * 
     * @param ontology
     *            The ontology to be saved.
     * @param ontologyFormat
     *            The format in which the ontology should be saved.
     * @throws OWLOntologyStorageException
     *             If the ontology cannot be saved.
     * @throws UnknownOWLOntologyException
     *             if the specified ontology is not managed by this manager */
    void saveOntology(@Nonnull OWLOntology ontology,
            @Nonnull OWLOntologyFormat ontologyFormat)
            throws OWLOntologyStorageException;

    /** Saves the specified ontology to the specified document IRI in the
     * specified ontology format.
     * 
     * @param ontology
     *            The ontology to be saved
     * @param ontologyFormat
     *            The format in which to save the ontology
     * @param documentIRI
     *            The document IRI where the ontology should be saved to
     * @throws OWLOntologyStorageException
     *             If the ontology could not be saved.
     * @throws UnknownOWLOntologyException
     *             if the specified ontology is not managed by the manager. */
    void
            saveOntology(@Nonnull OWLOntology ontology,
                    @Nonnull OWLOntologyFormat ontologyFormat,
                    @Nonnull IRI documentIRI)
                    throws OWLOntologyStorageException;

    /** Saves the specified ontology to the specified output stream in the
     * specified ontology format.
     * 
     * @param ontology
     *            The ontology to be saved
     * @param ontologyFormat
     *            The format in which to save the ontology
     * @param outputStream
     *            The output stream where the ontology will be saved to.
     * @throws OWLOntologyStorageException
     *             If the ontology could not be saved.
     * @throws UnknownOWLOntologyException
     *             if the specified ontology is not managed by the manager. */
    void saveOntology(@Nonnull OWLOntology ontology,
            @Nonnull OWLOntologyFormat ontologyFormat,
            @Nonnull OutputStream outputStream)
            throws OWLOntologyStorageException;

    /** Saves the specified ontology to the specified
     * {@link org.semanticweb.owlapi.io.OWLOntologyDocumentTarget}.
     * 
     * @param ontology
     *            The ontology to be saved.
     * @param documentTarget
     *            The output target where the ontology will be saved to.
     * @throws OWLOntologyStorageException
     *             If the ontology could not be saved.
     * @throws UnknownOWLOntologyException
     *             if the specified ontology is not managed by this manager. */
    void saveOntology(@Nonnull OWLOntology ontology,
            @Nonnull OWLOntologyDocumentTarget documentTarget)
            throws OWLOntologyStorageException;

    /** Saves the specified ontology to the specified output target in the
     * specified ontology format.
     * 
     * @param ontology
     *            The ontology to be saved.
     * @param ontologyFormat
     *            The output format in which to save the ontology
     * @param documentTarget
     *            The output target where the ontology will be saved to
     * @throws OWLOntologyStorageException
     *             If the ontology could not be saved.
     * @throws UnknownOWLOntologyException
     *             If the specified ontology is not managed by this manager. */
    void saveOntology(@Nonnull OWLOntology ontology,
            @Nonnull OWLOntologyFormat ontologyFormat,
            @Nonnull OWLOntologyDocumentTarget documentTarget)
            throws OWLOntologyStorageException;

    /** Set the collection of IRI mappers. It is used by Guice injection, but can
     * be used manually as well to replace the existing mappers with new ones.
     * 
     * @param mappers
     *            the mappers to be injected */
    void setIRIMappers(Set<OWLOntologyIRIMapper> mappers);

    /** Adds a mapper to this manager. The mapper is used to obtain ontology
     * document IRIs for ontology IRIs. The mapper will be added so that it is
     * given the highest priority (i.e. it will be tried first).
     * 
     * @param mapper
     *            The mapper to be added. */
    void addIRIMapper(@Nonnull OWLOntologyIRIMapper mapper);

    /** Removes a mapper from this manager.
     * 
     * @param mapper
     *            The mapper to be removed. If this manager does not managed the
     *            specified mapper then nothing will happen. */
    @Nonnull
    void removeIRIMapper(@Nonnull OWLOntologyIRIMapper mapper);

    /** Clears any installed IRI mappers */
    void clearIRIMappers();

    /** Set the collection of parsers. It is used by Guice injection, but can be
     * used manually as well to replace the existing parsers with new ones.
     * 
     * @param parsers
     *            the factories to be injected */
    void setOntologyParsers(Set<OWLParser> parsers);

    /** The collection of parsers. This set is a copy, changes are not reflected
     * back.
     * 
     * @return the available parsers */
    Set<OWLParser> getOntologyParsers();

    /** Set the collection of ontology factories. It is used by Guice injection,
     * but can be used manually as well to replace the existing factories with
     * new ones.
     * 
     * @param factories
     *            the factories to be injected */
    void setOntologyFactories(Set<OWLOntologyFactory> factories);

    /** Adds an ontology factory that is capable of creating an ontology given a
     * particular document IRI.
     * 
     * @param factory
     *            The factory to be added. */
    void addOntologyFactory(@Nonnull OWLOntologyFactory factory);

    /** Removes a previously added factory.
     * 
     * @param factory
     *            The factory to be removed. */
    @Nonnull
    void removeOntologyFactory(@Nonnull OWLOntologyFactory factory);

    /** Gets the ontology factories that are registered with this manager.
     * 
     * @return A collection of ontology factories. */
    @Nonnull
    Collection<OWLOntologyFactory> getOntologyFactories();

    /** Set the list of ontology storers. Storers will be sorted by priority.
     * 
     * @param storers
     *            The storers to be used */
    void setOntologyStorers(@Nonnull Set<OWLOntologyStorer> storers);

    /** The collection of storers. This set is a copy, changes are not reflected
     * back.
     * 
     * @return the available storers */
    Set<OWLOntologyStorer> getOntologyStorers();

    /** Adds an ontology change listener, which listens to all changes for all
     * ontologies. To customise the changes/ontologies that are listened to, the
     * {@code addOntologyChangeListener} method which takes a broadcast strategy
     * as an argument should be used.
     * 
     * @param listener
     *            The listener to be added. */
    void addOntologyChangeListener(@Nonnull OWLOntologyChangeListener listener);

    /** Adds an ontology change listener, which listens to ontology changes. An
     * ontology change broadcast strategy must be specified, which determines
     * the changes that are broadcast to the listener.
     * 
     * @param listener
     *            The listener to be added.
     * @param strategy
     *            The strategy that should be used for broadcasting changes to
     *            the listener. */
    void addOntologyChangeListener(@Nonnull OWLOntologyChangeListener listener,
            @Nonnull OWLOntologyChangeBroadcastStrategy strategy);

    /** @param listener
     *            the listener to add */
    void addImpendingOntologyChangeListener(
            @Nonnull ImpendingOWLOntologyChangeListener listener);

    /** @param listener
     *            the listener to remove */
    void removeImpendingOntologyChangeListener(
            @Nonnull ImpendingOWLOntologyChangeListener listener);

    /** @param listener
     *            the listener to add */
    void addOntologyChangesVetoedListener(
            @Nonnull OWLOntologyChangesVetoedListener listener);

    /** @param listener
     *            the listener to remove */
    void removeOntologyChangesVetoedListener(
            @Nonnull OWLOntologyChangesVetoedListener listener);

    /** Sets the default strategy that is used to broadcast ontology changes.
     * 
     * @param strategy
     *            The strategy to be used for broadcasting changes. This
     *            strategy will override any previously set broadcast strategy.
     *            The strategy should not be {@code null}.
     * @see org.semanticweb.owlapi.model.DefaultChangeBroadcastStrategy
     * @see org.semanticweb.owlapi.model.EDTChangeBroadcastStrategy */
    void setDefaultChangeBroadcastStrategy(
            @Nonnull OWLOntologyChangeBroadcastStrategy strategy);

    /** Removes a previously added listener.
     * 
     * @param listener
     *            The listener to be removed. */
    void removeOntologyChangeListener(
            @Nonnull OWLOntologyChangeListener listener);

    /** Requests that the manager loads an imported ontology that is described by
     * an imports statement. This method is generally used by parsers and other
     * kinds of loaders. For simply loading an ontology, use the loadOntologyXXX
     * methods. The method respects the list of ignored imports in the specified
     * configuration. In other words, if this methods is called for an ignored
     * import as specified by the configuration object then the import won't be
     * loaded.
     * 
     * @param declaration
     *            The declaration that describes the import to be loaded.
     * @param configuration
     *            The configuration object that passes arguments to the
     *            mechanism used for loading.
     * @throws UnloadableImportException
     *             if there was a problem creating and loading the import and
     *             silent missing imports handling is not turned on. If silent
     *             missing import handling is turned on then this exception will
     *             not be thrown. */
    void makeLoadImportRequest(@Nonnull OWLImportsDeclaration declaration,
            @Nonnull OWLOntologyLoaderConfiguration configuration)
            throws UnloadableImportException;

    /** In the case where silent missing imports handling is enabled, a listener
     * can be attached via this method so that there is a mechanism that allows
     * clients to be informed of the reason when an import cannot be loaded.
     * 
     * @param listener
     *            The listener to be added. */
    void addMissingImportListener(@Nonnull MissingImportListener listener);

    /** Removes a previously added missing import listener.
     * 
     * @param listener
     *            The listener to be removed. */
    void removeMissingImportListener(@Nonnull MissingImportListener listener);

    /** Adds an ontology loaded listener to this manager.
     * 
     * @param listener
     *            The listener to be added. */
    void addOntologyLoaderListener(@Nonnull OWLOntologyLoaderListener listener);

    /** Removes a previously added ontology loaded listener.
     * 
     * @param listener
     *            The listener to be removed. */
    void removeOntologyLoaderListener(
            @Nonnull OWLOntologyLoaderListener listener);

    /** Adds an ontology change progress listener.
     * 
     * @param listener
     *            The listener to be added. */
    void addOntologyChangeProgessListener(
            @Nonnull OWLOntologyChangeProgressListener listener);

    /** Removes a previously added ontology change listener.
     * 
     * @param listener
     *            The listener to be removed. */
    void removeOntologyChangeProgessListener(
            @Nonnull OWLOntologyChangeProgressListener listener);
}
